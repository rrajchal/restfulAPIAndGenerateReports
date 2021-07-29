package com.energy.restfulExample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	@Test
	void test() {
		CustomeCsvGenerator sort = new CustomeCsvGenerator("T");
		
		try {
			sort.generateCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	void testMode() {
		CustomeCsvGenerator t = new CustomeCsvGenerator("s");
		
		List <Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 2, 2, 3, 3, 3));
		List <Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 2, 3, 3, 3));
		
		List <Integer> arr1 = t.getModes(list1);
		List <Integer> arr2 = t.getModes(list2);
		
		for (Integer i : arr1) {
			System.out.println(i);
		}
		System.out.println();
		for (Integer i : arr2) {
			System.out.println(i);
		}
		
	}
	
	//@Test
	void testDecimal() {
		String regex = "\\d+.\\d+";
		String input1 = "123.12";
		String input2 = "12.3.12";
		String input3 = "123";
		String input4 = "";
		String input5 = "a";
		
		assertTrue(input1.matches(regex));
		assertFalse(input2.matches(regex));
		assertTrue(input3.matches(regex));
		assertFalse(input4.matches(regex));
		assertFalse(input5.matches(regex));
		
	}
	
	
	
	
	
    
}
