package com.anthem.platform.core.services.utility;

import java.text.ParseException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

public interface AssetXmlFeedService {

	public void writeAssets(XMLStreamWriter stream, Resource assetFolder, SlingHttpServletRequest request, String externalizerDomainName, Map<String,String> metadataMap, Map<String, String> tagMetadataMap, Map<String,String> configMap)
			throws XMLStreamException, ParseException;
	
	public void writePageData(XMLStreamWriter stream,SlingHttpServletRequest request,String externalizerDomainName, Map<String,String> metadataMap,  Map<String, String> tagMetadataMap, Map<String,String> configMap) throws XMLStreamException;
	
	public void writeBrightcoveVideoData(XMLStreamWriter stream,SlingHttpServletRequest request,String externalizerDomainName, Map<String,String> metadataMap, Map<String, String> tagMetadataMap, Map<String,String> configMap) throws XMLStreamException, ParseException;

}
