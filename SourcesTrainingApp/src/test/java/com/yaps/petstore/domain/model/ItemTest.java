package com.yaps.petstore.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.Activity;

public class ItemTest {

	private Event item ;
	
	@Before
	 public void setUp() {
		this.item = new Event("EST1499", "Young Male", new Date(1601000000000L), 156.0, new Activity("AVCB25"));
	 }
	
	@After
	 public  void tearDown(){
		this.item = null;
	 }

	@Test
	public void testGetAgeAsStringForBornYesterday() {
		item.setBirthDate(birthDateForAge(0, 0, 1));
		String age = item.getAgeAsString();
		assertEquals("1 jour(s)", age);
	}

	@Test
	public void testGetAgeAsStringForBaby() {
		item.setBirthDate(birthDateForAge(0, 0, 20));
		String age = item.getAgeAsString();
		assertEquals("20 jour(s)", age);
	}

	@Test
	public void testGetAgeAsStringForX() {
		item.setBirthDate(birthDateForAge(2, 3, 0));
		String age = item.getAgeAsString();
		assertEquals("2 an(s) et 3 mois", age);
	}

	@Test
	public void testGetAgeAsStringForY() {
		item.setBirthDate(birthDateForAge(0, 3, 5));
		String age = item.getAgeAsString();
		assertEquals("3 mois et 5 jour(s)", age);
	}


	@Test
	public void testGetAgeAsStringForOldCat() {
		item.setBirthDate(birthDateForAge(19, 1, 10));
		String age = item.getAgeAsString();
		assertEquals("19 an(s) 1 mois et 10 jour(s)", age);
	}

	@Test
	public void testLocalDate_toEpochDay() {
		//  Epoch day is 01-01-1970		
		LocalDate epochDay = LocalDate.of(1970, 1, 1);
		long days = epochDay.toEpochDay();
    	assertEquals(0, days);
    	
    	LocalDate epochDayPlus5 = epochDay.plusDays(5);
    	days = epochDayPlus5.toEpochDay();
    	assertEquals(5, days);
    	
    	LocalDate now = LocalDate.now();
		long daysSinceEpochDay = now.toEpochDay();
		int currentYear = now.getYear();
		int yearsSinceEpochDay = currentYear - 1970;
		assertTrue(yearsSinceEpochDay*365 < daysSinceEpochDay);
		assertTrue((yearsSinceEpochDay+1)*365 > daysSinceEpochDay);	
	}

	@Test
	public void testBirthDateForAge() {
		java.sql.Date now = birthDateForAge(0, 0, 0);
    	LocalDate ld = now.toLocalDate();
    	long daysNow1 = LocalDate.now().toEpochDay();
    	long daysNow2 = ld.toEpochDay();
    	assertEquals(daysNow1, daysNow2);
	}

	private java.sql.Date birthDateForAge(int years, int months, int days) {
		LocalDate ld = LocalDate.now();
    	long daysToSubtract = years * 365 + months * 30 + days;
    	ld = ld.minusDays(daysToSubtract);
		java.sql.Date date = java.sql.Date.valueOf(ld);	
		return date;
	}

}
