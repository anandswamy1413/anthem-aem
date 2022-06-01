package com.anthem.platform.core.services.utility.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;


@Component(immediate = true,
        service = {TransformerFactory.class},
        enabled = true, property = {"pipeline.type=linkrewriterhtml"})
@Designate(ocd = LinkRewriterTransformerFactory.Config.class)
public class LinkRewriterTransformerFactory implements TransformerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkRewriterTransformerFactory.class);
    private final String HREF = "href";
    private final String SRC = "src";
    private final String PREFIX = "/v2";
    private Config config;
    private boolean enableRewriter;
    private String contentPath;
    private String damPath;

    public Transformer createTransformer() {
        return new LinkRewriterTransformer();
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
    }

    @ObjectClassDefinition(name = "Anthem Link ReWriter", description = "Utility for changing the output DOM")
    public @interface Config {

        @AttributeDefinition(name = "Enable Link Rewriting", description = "true or false")
        boolean enableRewrite() default false;

        @AttributeDefinition(name = "Content Path", description = "Content Path to process")
        String contentPath() default "/etc.clientlibs";

        @AttributeDefinition(name = "Anthem DAM Path", description = "DAM Path to process")
        String damPath() default "/content";

    }

    private class LinkRewriterTransformer implements Transformer {
        private ContentHandler contentHandler;
        private SlingHttpServletRequest request;

        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        public void dispose() {
        }

        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            contentHandler.endElement(uri, localName, qName);
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
            request = context.getRequest();
            Resource contentResource = request.getResource();
            contentResource.adaptTo(ConfigurationBuilder.class);
        }

        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            final AttributesImpl attributes = new AttributesImpl(atts);
            final String href = attributes.getValue(HREF);
            final String src = attributes.getValue(SRC);
            if (config.enableRewrite()) {
                for (int i = 0; i < attributes.getLength(); i++) {

                    if ("href".equalsIgnoreCase(attributes.getQName(i)) && StringUtils.isNotBlank(href) && (StringUtils.contains(href, config.contentPath()) || StringUtils.contains(href, config.damPath()))) {
                        String newPath = StringUtils.prependIfMissing(href, PREFIX);
                        LOGGER.debug("New Path href > {}", newPath);
                        attributes.setValue(i, newPath);
                        break;
                    } else if ("src".equalsIgnoreCase(attributes.getQName(i)) && StringUtils.isNotBlank(src) && (StringUtils.contains(src, config.contentPath()) || StringUtils.contains(src, config.damPath()))) {
                        String newPath = StringUtils.prependIfMissing(src, PREFIX);
                        LOGGER.debug("New Path src > {}", newPath);
                        attributes.setValue(i, newPath);
                        break;
                    }
                }
            }
            contentHandler.startElement(uri, localName, qName, attributes);
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }
    }
}