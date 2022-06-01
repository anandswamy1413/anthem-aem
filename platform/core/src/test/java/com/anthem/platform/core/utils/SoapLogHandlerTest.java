package com.anthem.platform.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

class SoapLogHandlerTest {

	@InjectMocks
	private SoapLogHandler soaploghandler;
	
	@Mock
	private SOAPMessageContext smc;
	
	@Mock
	private MessageContext messageContext;
	
	@Mock
	private SOAPMessage message;
	
	@InjectMocks
	private Source soapContent=new StreamSource();
	
	@Mock
	private ByteArrayOutputStream streamOut;
	
	@Mock
    private StreamResult result;
	
	@Mock
	private SOAPPart soapPart;
	
	@Mock
	private Transformer transformer;
	
	@Mock
	private Logger log;
	
	@BeforeEach
	void setUp() throws Exception
	{ MockitoAnnotations.initMocks(this);
	  when(smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(true);
	  when(smc.getMessage()).thenReturn(message);
	  when(message.getSOAPPart()).thenReturn(soapPart);
	  when(soapPart.getContent()).thenReturn(soapContent);
	 
	}

	 /**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.SoapLogHandler#getHeaders()}.
	  */
	@Test
	void testGetHeaders()
	{
	   assertEquals(Collections.EMPTY_SET, soaploghandler.getHeaders());
	}

	/**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.SoapLogHandler#handleMessage()}.
	  */	
	@Test
	void testHandleMessage() 
	{
	    assertTrue((soaploghandler.handleMessage(smc)));
	    
	}
 
	/**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.SoapLogHandler#handleFault()}.
	  */
	@Test
	void testHandleFault() 
	{
		assertTrue(soaploghandler.handleFault(smc));
	}

	/**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.SoapLogHandler#close()}.
	  */	
	@Test
	void testClose() {
		soaploghandler.close(messageContext);
		assertNotNull(soaploghandler);

	}

}
