package com.anthem.platform.core.utils;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

@SuppressWarnings("squid:S2221")
public class RteHtmlFilter extends WCMUsePojo{

	private static final Logger LOG = LoggerFactory.getLogger(RteHtmlFilter.class);

	private static final String RICHTEXT = "richtext";

	private String iconHtml = "&nbsp;<span class=\"rte-icon {0}\"></span>";

	private String html;

	@Override
	public void activate() throws Exception {
		html = get(RICHTEXT, String.class);
	}

	// Add filter methods
	public String getFilteredHtml() {
		Document doc = Jsoup.parse(html, "", Parser.xmlParser());
		linkFilter(doc);
		return doc.toString();
	}

	private void linkFilter(Document doc) {
		String htmlSelectors = "a";
		String iconAttribute = "data-icons";
		String iconPosAttribute = "data-iconpos";
		String hrTag = "hr + br";
		try {
			Elements links = doc.select(htmlSelectors);
			for (Element link : links) {
				String iconAttrValue = link.attr(iconAttribute);
				String iconposAttrValue = link.attr(iconPosAttribute);
				Elements span = link.getElementsByTag("span");
				String siblingClass = "";
				if(null != span) {
					siblingClass = span.attr("class");
				}
				if(StringUtils.isNotBlank(iconAttrValue)) {
					if(StringUtils.equalsIgnoreCase(iconposAttrValue, "start")) {
						link.prepend(MessageFormat.format(iconHtml, iconAttrValue + " " + siblingClass));
					} else {
						link.append(MessageFormat.format(iconHtml, iconAttrValue + " " + siblingClass));
					}
				}
			}

			Elements hrTags = doc.select(hrTag);
			for (Element tag : hrTags) {
				tag.remove();
			}
		} catch (Exception e) {
			LOG.error("Invalid input: {}", htmlSelectors, new Exception("Invalid input"));
		}
	}
}