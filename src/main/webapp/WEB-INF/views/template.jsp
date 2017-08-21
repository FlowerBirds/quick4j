<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta charset="UTF-8">
        <title></title>
    </head>
    <body>
    <!-- 		<div class="forget-password">
			<h4>忘记密码 ?</h4>
			<p>点击 <a href="javascript:;" id="forget-password">这里</a> 重置您的密码.
			</p>
		</div>
		<div class="create-account">
			<p>
				 还没有账号 ?&nbsp; <a href="javascript:;" id="register-btn">创建一个账号</a>
			</p>
		</div> -->
    
    
    
    
	<!-- BEGIN REGISTRATION FORM -->
	<!-- 
	<form class="register-form" action="index.html" method="post">
		<h3>注  册</h3>
		<p>
			 请输入您的个人信息:
		</p>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">全名</label>
			<div class="input-icon">
				<i class="fa fa-font"></i>
				<input class="form-control placeholder-no-fix" type="text" placeholder="全名" name="fullname"/>
			</div>
		</div>
		<div class="form-group">
			
			<label class="control-label visible-ie8 visible-ie9">Email</label>
			<div class="input-icon">
				<i class="fa fa-envelope"></i>
				<input class="form-control placeholder-no-fix" type="text" placeholder="Email" name="email"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">地址</label>
			<div class="input-icon">
				<i class="fa fa-check"></i>
				<input class="form-control placeholder-no-fix" type="text" placeholder="地址" name="address"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">城市</label>
			<div class="input-icon">
				<i class="fa fa-location-arrow"></i>
				<input class="form-control placeholder-no-fix" type="text" placeholder="城市" name="city"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">国家</label>
			<select name="country" id="select2_sample4" class="select2 form-control">
				<option value="CN"></option>
			</select>
		</div>
		<p>
			 请输入账号信息:
		</p>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">用户名</label>
			<div class="input-icon">
				<i class="fa fa-user"></i>
				<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="用户名" name="username"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">密码</label>
			<div class="input-icon">
				<i class="fa fa-lock"></i>
				<input class="form-control placeholder-no-fix" type="password" autocomplete="off" id="register_password" placeholder="密码" name="password"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">再次输入密码</label>
			<div class="controls">
				<div class="input-icon">
					<i class="fa fa-check"></i>
					<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="再次输入密码" name="rpassword"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label>
			<input type="checkbox" name="tnc"/> 我同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a>
			</label>
			<div id="register_tnc_error">
			</div>
		</div>
		<div class="form-actions">
			<button id="register-back-btn" type="button" class="btn">
			<i class="m-icon-swapleft"></i> 返回 </button>
			<button type="submit" id="register-submit-btn" class="btn blue pull-right">
			注册 <i class="m-icon-swapright m-icon-white"></i>
			</button>
		</div>
	</form>
	 -->
	<!-- END REGISTRATION FORM -->
    </body>
</html>