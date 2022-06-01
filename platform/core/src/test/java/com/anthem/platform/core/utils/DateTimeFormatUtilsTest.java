package com.anthem.platform.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class DateTimeFormatUtilsTest {
	
	
	@InjectMocks
	DateTimeFormatUtils dateUtils;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	
	@BeforeEach
	void setUp() throws ParseException {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testLongToTimeStamp() {
		dateUtils.convertLongToTimeStamp(2323434);
	}
	
	@Test
	void testStringToTimeStamp() throws ParseException {
		dateUtils.stringToTimeStamp("2021-05-01T09:31:46.626-04:00");
	}

}
