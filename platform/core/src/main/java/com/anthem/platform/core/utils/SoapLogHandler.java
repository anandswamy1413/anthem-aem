package com.anthem.platform.core.utils;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapLogHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger LOG = LoggerFactory.getLogger(SoapLogHandler.class);

	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);		
		SOAPMessage message = context.getMessage();
		try {
			if (isRequest.booleanValue()) {
				LOG.info("==== SOAP Request ===");
			} else {
				LOG.info("==== SOAP Response ===");
			}
			String msg=printSoapMessage(message);
			LOG.info(msg);
		} catch (SOAPException | TransformerFactoryConfigurationError | TransformerException e) {
			LOG.error("Error logging soap messages ",e);
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	
	public void close(MessageContext messageContext) {
		// nothing to clean up
	}

	private String printSoapMessage(final SOAPMessage soapMessage) throws TransformerFactoryConfigurationError, SOAPException, TransformerException {
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		//Allowing access to external entities in XML parsing could lead to confidential file disclosures.To protect Java XML Parsers three lines have been added
		transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		final Source soapContent = soapMessage.getSOAPPart().getContent();
		final ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		final StreamResult result = new StreamResult(streamOut);
		transformer.transform(soapContent, result);
		return streamOut.toString();
	}
}
