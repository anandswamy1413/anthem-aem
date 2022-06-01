package com.anthem.platform.core.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true, service = DateTimeFormatUtils.class)
public class DateTimeFormatUtils {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public Timestamp stringToTimeStamp(String timeStamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		try {
			Date date = format.parse(timeStamp);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			log.error("Timestamp format exception ", e);
		}
		return null;
		   
	}
	
	public String convertLongToTimeStamp(long time){
	    Date date = new Date(time);
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	    return format.format(date);
	}
}
