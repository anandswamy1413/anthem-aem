package com.anthem.platform.core.services.utility;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.wcm.api.Page;

public interface PageXmlFeedService {

	boolean isValidPageTemplate(Page page);

	String externalizeUri(SlingHttpServletRequest request, String path,String externalizerKey);

	void write(Page page, XMLStreamWriter stream,
			SlingHttpServletRequest request, String NS, String externalizerKey)
			throws XMLStreamException;

	void writeElement(XMLStreamWriter stream, String elementName, String text,
			String NS) throws XMLStreamException;

}
