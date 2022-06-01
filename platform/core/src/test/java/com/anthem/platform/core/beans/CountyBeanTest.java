package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class CountyBeanTest {

	private static final String STATE_CODE = "state code";
	private static final String ZIP_CODE = "400001";
	private static final String COUNTY_CODE = "county code";
	private static final String COUNTY_NAME = "county name";
	private static final String CITY_NAME = "city name";
	
	@InjectMocks
	CountyBean countyBean;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		countyBean.setStateCode(STATE_CODE);
		countyBean.setZipCode(ZIP_CODE);
		countyBean.setCountyCode(COUNTY_CODE);
		countyBean.setCountyName(COUNTY_NAME);
		countyBean.setCityName(CITY_NAME);
	}

	@Test
	void testGetStateCode() {
		String actual = "state code";
		String expected = countyBean.getStateCode();
		assertNotNull(countyBean);
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetZipCode() {
		String actual = "400001";
		String expected = countyBean.getZipCode();
		assertNotNull(countyBean);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCountyCode() {
		String actual = "county code";
		String expected = countyBean.getCountyCode();
		assertNotNull(countyBean);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCountyName() {
		String actual = "county name";
		String expected = countyBean.getCountyName();
		assertNotNull(countyBean);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCityName() {
		String actual = "city name";
		String expected = countyBean.getCityName();
		assertNotNull(countyBean);
		assertEquals(expected, actual);
	}
}
