package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.apache.sling.xss.XSSAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonMapperServiceImplTest {

	private static final T T = new T();

	@Mock
	XSSAPI XSSAPI;

	@InjectMocks
	JacksonMapperServiceImpl jacksonMapperServiceImpl;

	@Mock
	InputStream inputStream;

	@Mock
	InputStream stream;

	@Mock
	Map<String, Object> map;

	Map<String, T> jsonMap = new HashMap<>();

	@Mock
	Logger logger;

	@Mock
	ObjectMapper mapper;

	Class<T> clazz;

	@Mock
	TypeReference<HashMap<String, Object>> typeRef;

	@BeforeEach
	void setUp() throws JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.initMocks(this);
		when(mapper.readValue("jsonString", Map.class)).thenReturn(map);
		when(mapper.convertValue(clazz, typeRef)).thenReturn(map);
		when(mapper.readValue(inputStream, clazz)).thenReturn(T);
		when(mapper.readValue("jsonString", clazz)).thenReturn(T);
		when(mapper.writeValueAsString(clazz)).thenReturn("jsonString");
	}

	@Test
	void testConvertJsonToMap() throws Exception {
		jacksonMapperServiceImpl.convertJsonToMap("jsonString");
		assertEquals(map, mapper.readValue("jsonString", Map.class));
	}

	@Test
	void ConvertObjectToMap() {
		when(mapper.convertValue(clazz, typeRef)).thenReturn(map);
		assertEquals(map, mapper.convertValue(clazz, typeRef));
		jacksonMapperServiceImpl.convertObjectToMap(T, false);
	}

	@Test
	void testConvertObjectToJson() throws JsonProcessingException {
		when(mapper.writeValueAsString(clazz)).thenReturn("jsonString");
		assertEquals("jsonString", mapper.writeValueAsString(clazz));
		jacksonMapperServiceImpl.convertObjectToJson(T, true);
	}

	@Test
	void testConvertJsonToObject() throws JsonParseException, JsonMappingException, IOException {
		when(mapper.readValue(inputStream, clazz)).thenReturn(T);
		assertEquals(T, mapper.readValue(inputStream, clazz));
		jacksonMapperServiceImpl.convertJsonToObject(inputStream, Class.class);
	}

	@Test
	void testConvertJsonToobject() throws JsonParseException, JsonMappingException, IOException {
		when(mapper.readValue("jsonString", clazz)).thenReturn(T);
		assertEquals(T, mapper.readValue("jsonString", clazz));
		jacksonMapperServiceImpl.convertJsonToObject("jsonString", Class.class);
	}

	@Test
	void testSanitvizedJsonString() {
		when(XSSAPI.getValidJSON("jsonString", "{}")).thenReturn(Mockito.anyString());
		jacksonMapperServiceImpl.sanitizedJsonString(Mockito.anyString());
		assertNotNull(jacksonMapperServiceImpl);
	}

	@Test
	void testSanitizeInputStreamToObject() throws IOException {
		jacksonMapperServiceImpl.sanitizeInputStreamToObject(inputStream, Class.class);
		assertNotNull(jacksonMapperServiceImpl);
	}

	@Test
	void testmapToJson() throws IOException {
		when(mapper.writeValueAsString(jsonMap)).thenReturn("mapToJson");
		jacksonMapperServiceImpl.mapToJson(jsonMap);
		assertNotNull(jacksonMapperServiceImpl);
	}
}
