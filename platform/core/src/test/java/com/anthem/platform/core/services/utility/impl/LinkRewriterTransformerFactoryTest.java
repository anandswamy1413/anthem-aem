package com.anthem.platform.core.services.utility.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

 class LinkRewriterTransformerFactoryTest {

    @InjectMocks
    private LinkRewriterTransformerFactory
            linkRewriterTransformerFactory;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private Resource resource;

    @Mock
    private LinkRewriterTransformerFactory.Config config;

    @Mock
    private AttributesImpl attributes;

    @Mock
    private ContentHandler contentHandler;

    @Mock
    private ProcessingContext context;

    @Mock
    private Locator locator;

    @Mock
    private ProcessingComponentConfiguration processingComponentConfiguration;

    @BeforeEach
    public void setup() throws SAXException {
        MockitoAnnotations.initMocks(this);

        when(context.getRequest()).thenReturn(request);
        when(request.getResource()).thenReturn(resource);
        when(config.enableRewrite()).thenReturn(true);
        when(config.damPath()).thenReturn("/content");
        when(config.contentPath()).thenReturn("/etc.clientlibs");
        linkRewriterTransformerFactory.activate(config);
    }

    @Test
    void TestLinkRewrite() throws SAXException, IOException {
        Transformer transformer = linkRewriterTransformerFactory.createTransformer();
        transformer.setContentHandler(contentHandler);
        AttributesImpl in = new AttributesImpl();
        in.addAttribute("", "src", "",
                "CDATA", "/etc/clientlib/test.css");
        in.addAttribute(StringUtils.EMPTY, "target", StringUtils.EMPTY, "CDATA", "_blank");
        in.addAttribute(StringUtils.EMPTY, "href", StringUtils.EMPTY, "CDATA", "/etc/clientlib/test.css");
        transformer.startElement(null, "div",
                null, in);
        transformer.characters("TEST_DATA".toCharArray(), 0, "TEST_DATA".length());
        transformer.dispose();
        transformer.endDocument();
        transformer.endElement(StringUtils.EMPTY, "href", StringUtils.EMPTY);
        transformer.endPrefixMapping(StringUtils.EMPTY);
        transformer.init(context, processingComponentConfiguration);
        transformer.processingInstruction(StringUtils.EMPTY, StringUtils.EMPTY);
        transformer.setDocumentLocator(locator);
        transformer.skippedEntity(StringUtils.EMPTY);
        transformer.startDocument();
        transformer.ignorableWhitespace("TEST_DATA".toCharArray(), 0, "TEST_DATA".length());
        transformer.startPrefixMapping(StringUtils.EMPTY, StringUtils.EMPTY);
        contentHandler.startElement(null, "a",
                null, in);

        assertEquals(3,in.getLength());
    }

    @Test
    void TestLinkRewriteSrc() throws SAXException {
        Transformer transformer = linkRewriterTransformerFactory.createTransformer();
        transformer.setContentHandler(contentHandler);
        AttributesImpl in = new AttributesImpl();
        in.addAttribute("", "src", "src",
                "CDATA", "/etc.clientlibs/test.css");
        transformer.startElement("/content/anthem/us", "div",
                "src", in);
        contentHandler.startElement(null, "a",
                null, in);

        assertEquals(1,in.getLength());
    }

    @Test
    void TestLinkRewriteHref() throws SAXException {
        Transformer transformer = linkRewriterTransformerFactory.createTransformer();
        transformer.setContentHandler(contentHandler);
        AttributesImpl in = new AttributesImpl();
        in.addAttribute("", "href", "href",
                "CDATA", "/content/testPage");
        transformer.startElement("/content/anthem/us", "div",
                "href", in);
        contentHandler.startElement(null, "a",
                null, in);

        assertEquals(1,in.getLength());
    }

}
