package com.anthem.platform.core.services.utility.impl;

import java.util.Calendar;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.PageXmlFeedService;
import com.anthem.platform.core.services.utility.configs.PageXmlFeedConfigs;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

@Designate(ocd = PageXmlFeedConfigs.class, factory = true)
@Component(immediate = true, service = PageXmlFeedService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PageXmlFeedServiceImpl implements PageXmlFeedService {

	private static final Logger log = LoggerFactory.getLogger(PageXmlFeedServiceImpl.class);
	
	String[] includePageTemplates;
	@Reference
	private transient Externalizer externalizer;

	

	private static final FastDateFormat DATE_FORMAT = FastDateFormat
			.getInstance("yyyy-MM-dd");
	
	@Activate
	public void activate(PageXmlFeedConfigs config) {
		this.includePageTemplates = config.allowedTemplates();
		
	}
	

	
	@Override
	public void write(Page page, XMLStreamWriter stream,
			SlingHttpServletRequest request, String NS, String externalizerKey) throws XMLStreamException {
		if (!isValidPageTemplate(page)) {
			return;
		}
		stream.writeStartElement(NS, "url");
		String loc = "";

		loc = externalizeUri(request, String.format("%s.html", page.getPath()),externalizerKey);

		writeElement(stream, PlatformConstants.LOCATION, loc, NS);

		Calendar cal = page.getLastModified();
		if (cal != null) {
			writeElement(stream, PlatformConstants.LAST_MODIFIED, DATE_FORMAT.format(cal), NS);
		}

		stream.writeEndElement();
	}
	
	@Override
	public boolean isValidPageTemplate(Page page) {
		boolean flag = false;
		if (this.includePageTemplates != null) {
			for (String pageTemplate : this.includePageTemplates) {
				flag = flag
						|| page.getProperties()
								.get("cq:template", StringUtils.EMPTY)
								.equalsIgnoreCase(pageTemplate);
			}
		}
		return flag;
	}
	
	@Override
	public String externalizeUri(SlingHttpServletRequest request, String path,String externalizerKey) {
		if (StringUtils.isNotBlank(externalizerKey)) {
			return externalizer.externalLink(request.getResourceResolver(),
					externalizerKey, path);
		} else {
			log.debug(
					"No externalizer domain configured, take into account current host header {} and current scheme {}",
					request.getServerName(), request.getScheme());
			return externalizer
					.absoluteLink(request, request.getScheme(), path);
		}
	}
	
	@Override
	public void writeElement(final XMLStreamWriter stream,
			final String elementName, final String text, String NS)
			throws XMLStreamException {
		stream.writeStartElement(NS, elementName);
		stream.writeCharacters(text);
		stream.writeEndElement();
	}
}
