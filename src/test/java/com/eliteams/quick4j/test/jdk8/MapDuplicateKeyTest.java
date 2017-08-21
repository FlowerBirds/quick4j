/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年8月3日
 * 
 */
package com.eliteams.quick4j.test.jdk8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class MapDuplicateKeyTest {

	@Test
	public void mapkey() {
		List<Entity> list = new ArrayList<>();
		list.add(new Entity("20170728120", 1));
		list.add(new Entity("20170728119", 3));
		list.add(new Entity("20170728119", 2));
		
		Map<String, Integer> map = list.stream().collect(
				Collectors.toMap(Entity::getKey, Entity::getValue, (s, a) -> s + a));
		
		
		map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
		
	}

}
