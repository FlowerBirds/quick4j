/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月28日
 * 
 */
package com.eliteams.quick4j.web.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * @author Administrator
 *
 */
public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	/** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
	public static final String CIPHER_ALGORITHM = "RSA/ECB/NoPadding";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";

	/** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
	public static final int KEY_SIZE = 1024;

	public static final String PLAIN_TEXT = "aaaa";
	private static final String ENCODE_TEXT = "cE3D4ub4aj9ck3gu4V601fvGRQ8p4Vs76Gl8+VPKxdZ0mZYLQjeownnLWKOHPEQIyao4JXzgcIw00onNLEgWGE5y43XgneG/XRQD0omZEbNpMUlp/tGOUQE4K9MmpAHxtHObGbauY8ssASGGpv3eZwYJ6BeEfQi1EYRABpmJ54o=";
	
	private static byte[] priKey = null;
	private static byte[] pubKey = null;
	
	private static Cipher decryptCipher = null;
	private static Cipher encryptCipher = null;

	public static void main(String[] args) throws Exception {
		Map<String, byte[]> keyMap = generateKeyBytes();

		System.out.println(keyMap.get(PUBLIC_KEY));
		// 加密
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));

		byte[] encodedText = RSAEncode(publicKey, PLAIN_TEXT.getBytes("utf-8"));
		System.out.println("RSA encoded: " + Base64.encodeBase64String(encodedText));

		// 解密
		PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
		System.out.println("RSA decoded: " + RSADecode(privateKey, encodedText, "utf-8"));
		System.out.println(DigestUtils.md5Hex("aaa"));
		//saveRsaAsFile("F:\\aliyun\\key");
		System.out.println(MD5Util.digest("F:\\\\aliyun\\\\key\\id_rsa"));
		
		String publicKeyString = getRSAPublicKeyAsNetFormat(publicKey.getEncoded());  
        String privateKeyString = getRSAPrivateKeyAsNetFormat(privateKey.getEncoded()); 
        System.out.println(encodeBase64(publicKey.getEncoded()));
        System.out.println(encodePublicKeyToXml(publicKey));  
        System.out.println(publicKeyString);  
        System.out.println(privateKeyString); 
        
        byte[] keybytes = IOUtil.read("F:\\aliyun\\key\\id_rsa");
        PrivateKey privKey = restorePrivateKey(keybytes);
        System.out.println("RSA decoded: " + RSADecode(privKey, decodeBase64(ENCODE_TEXT), "utf-8"));
        
        byte[] pubbytes = IOUtil.read("F:\\aliyun\\key\\id_rsa.pub");
        PublicKey pubKey = restorePublicKey(pubbytes);
        byte[] eText = RSAEncode(pubKey, PLAIN_TEXT.getBytes("utf-8"));
        String bText = Base64.encodeBase64String(eText);
        System.out.println("RSA encoded: " + bText);
        
        System.out.println("RSA decoded: " + RSADecode(privKey, decodeBase64(bText), "utf-8"));
        
	}

	/**
	 * 使用公钥加密
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String content) 
			throws UnsupportedEncodingException {
		PublicKey publicKey = restorePublicKey(pubKey);
		byte[] encodedText = RSAEncode(publicKey, content.getBytes("utf-8"));
		return Base64.encodeBase64String(encodedText);
	}
	
	/**
	 * 解密公钥加密的内容
	 * @param content
	 * @return
	 */
	public static String decode(String content) {
		PrivateKey privateKey = restorePrivateKey(priKey);
		byte[] encodedText =Base64.decodeBase64(content);
		return RSADecode(privateKey, encodedText, "utf-8");
	}
	
	/**
	 * 使用已经实例化的解析器
	 * @param content
	 * @return
	 */
	public static String decodeFast(String content) {
		byte[] encodedText = Base64.decodeBase64(content);
		try {
			return new String(decryptCipher.doFinal(encodedText));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 使用已经实例化的加密实例
	 * @param content
	 * @return
	 */
	public static String encodeFast(String content) {
		byte[] bytes;
		try {
			bytes = content.getBytes("utf-8");
			byte[] encypts =  encryptCipher.doFinal(bytes);
			return Base64.encodeBase64String(encypts);
		} catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 加载公钥和私钥
	 * @param priFile
	 * @param pubFile
	 */
	public static void load(String priFile, String pubFile) {
		priKey = IOUtil.read(priFile);
		pubKey = IOUtil.read(pubFile);
		
		try {
			PrivateKey privateKey = restorePrivateKey(priKey);
			decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM, new BouncyCastleProvider());
			decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			PublicKey publicKey = restorePublicKey(pubKey);
			encryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
	 * 
	 * @return
	 */
	public static Map<String, byte[]> generateKeyBytes() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
			keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
			keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密，三步走。
	 * 
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static byte[] RSAEncode(PublicKey key, byte[] plainText) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解密，三步走。
	 * 
	 * @param key
	 * @param encodedText
	 * @return
	 */
	public static String RSADecode(PrivateKey key, byte[] encodedText, String charset) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(encodedText), charset);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 保存公钥和私钥到文件夹中
	 * @param path
	 */
	public static void saveRsaAsFile(String path) {
		File dir = new File(path);
		if(!dir.exists() || !dir.isDirectory()) {
			throw new RuntimeException("Path not found or not a directory :" + path);
		}
		Map<String, byte[]> keyMap = generateKeyBytes();
		byte[] pubKey = keyMap.get(PUBLIC_KEY);
		IOUtil.save(new File(dir, "id_rsa.pub"), pubKey);
		byte[] priKey = keyMap.get(PRIVATE_KEY);
		IOUtil.save(new File(dir, "id_rsa"), priKey);
		String publicKeyString = getRSAPublicKeyAsNetFormat(pubKey);
		IOUtil.save(new File(dir, "id_rsa_c.pub"), publicKeyString);
        String privateKeyString = getRSAPrivateKeyAsNetFormat(priKey); 
        IOUtil.save(new File(dir, "id_rsa_c"), privateKeyString);
	}
	

    /** 
     * 私钥转换成C#格式 
     * @param encodedPrivkey 
     * @return 
     */  
    public static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivateKey) {  
        try {  
            StringBuffer buff = new StringBuffer(1024);  
  
            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(  
                    encodedPrivateKey);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory  
                    .generatePrivate(pvkKeySpec);  
  
            buff.append("<RSAKeyValue>");  
            buff.append("<Modulus>"  
                    + encodeBase64(removeMSZero(pvkKey.getModulus()  
                            .toByteArray())) + "</Modulus>");  
  
            buff.append("<Exponent>"  
                    + encodeBase64(removeMSZero(pvkKey.getPublicExponent()  
                            .toByteArray())) + "</Exponent>");  
  
            buff.append("<P>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeP()  
                            .toByteArray())) + "</P>");  
  
            buff.append("<Q>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeQ()  
                            .toByteArray())) + "</Q>");  
  
            buff.append("<DP>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentP()  
                            .toByteArray())) + "</DP>");  
  
            buff.append("<DQ>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentQ()  
                            .toByteArray())) + "</DQ>");  
  
            buff.append("<InverseQ>"  
                    + encodeBase64(removeMSZero(pvkKey.getCrtCoefficient()  
                            .toByteArray())) + "</InverseQ>");  
  
            buff.append("<D>"  
                    + encodeBase64(removeMSZero(pvkKey.getPrivateExponent()  
                            .toByteArray())) + "</D>");  
            buff.append("</RSAKeyValue>");  
  
            return buff.toString();  
        } catch (Exception e) {  
            System.err.println(e);  
            return null;  
        }  
    }  
    
    /** 
     * 公钥转成C#格式 
     * @param encodedPrivkey 
     * @return 
     */  
    public static String getRSAPublicKeyAsNetFormat(byte[] encodedPublicKey) {  
        try {  
            StringBuffer buff = new StringBuffer(1024);  
              
            //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKey pukKey = (RSAPublicKey) keyFactory  
                    .generatePublic(new X509EncodedKeySpec(encodedPublicKey));  
  
            buff.append("<RSAKeyValue>");  
            buff.append("<Modulus>"  
                    + encodeBase64(removeMSZero(pukKey.getModulus()  
                            .toByteArray())) + "</Modulus>");  
            buff.append("<Exponent>"  
                    + encodeBase64(removeMSZero(pukKey.getPublicExponent()  
                            .toByteArray())) + "</Exponent>");  
            buff.append("</RSAKeyValue>");  
            return buff.toString();  
        } catch (Exception e) {  
            System.err.println(e);  
            return null;  
        }  
    }  
    
    /** 
     * 公钥转换成C#格式 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static String encodePublicKeyToXml(PublicKey key) throws Exception {  
        if (!RSAPublicKey.class.isInstance(key)) {  
            return null;  
        }  
        RSAPublicKey pubKey = (RSAPublicKey) key;  
        StringBuilder sb = new StringBuilder();  
  
        sb.append("<RSAKeyValue>");  
        sb.append("<Modulus>")  
                .append(encodeBase64(removeMSZero(pubKey.getModulus()  
                        .toByteArray()))).append("</Modulus>");  
        sb.append("<Exponent>")  
                .append(encodeBase64(removeMSZero(pubKey.getPublicExponent()  
                        .toByteArray()))).append("</Exponent>");  
        sb.append("</RSAKeyValue>");  
        return sb.toString();  
    }
    
    /** 
     * @param data 
     * @return 
     */  
    private static byte[] removeMSZero(byte[] data) {  
        byte[] data1;  
        int len = data.length;  
        if (data[0] == 0) {  
            data1 = new byte[data.length - 1];  
            System.arraycopy(data, 1, data1, 0, len - 1);  
        } else  
            data1 = data;  
  
        return data1;  
    } 
    /** 
     * base64编码 
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static String encodeBase64(byte[] input) throws Exception {  
        Class<?> clazz = Class  
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod = clazz.getMethod("encode", byte[].class);  
        mainMethod.setAccessible(true);  
        Object retObj = mainMethod.invoke(null, new Object[] { input });  
        return (String) retObj;  
    }  
  
    /** 
     * base64解码  
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decodeBase64(String input) throws Exception {  
        Class<?> clazz = Class  
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod = clazz.getMethod("decode", String.class);  
        mainMethod.setAccessible(true);  
        Object retObj = mainMethod.invoke(null, input);  
        return (byte[]) retObj;  
    }  
}
