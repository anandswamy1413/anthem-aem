package com.anthem.platform.core.services.utility.impl;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.query.Query;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.AssetXmlFeedService;
import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.configs.AssetXmlFeedConfigs;
import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Designate(ocd = AssetXmlFeedConfigs.class, factory = true)
@Component(immediate = true, service = AssetXmlFeedService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AssetXmlFeedServiceImpl implements AssetXmlFeedService {

	private static final Logger LOG = LoggerFactory.getLogger(AssetXmlFeedServiceImpl.class);
	
	private static final String INTERNAL_DOMAIN = "publish"; 

	private String[] assetTypes;

	private String[] pagePaths;

	private String[] brightcoveVideoPaths;

	private String applicationTagId;

	private boolean isExtensionlessUrl;

	private String thumbnailListPath;

	private static final String NS = "";
	private static final String DAM_METADATA = "metadata";

	private MessageFormat pageQueryFormatter = new MessageFormat(
			"SELECT * FROM [cq:PageContent] AS pageContent WHERE ({0}) AND pageContent.[articleTypeTag] IS NOT NULL");

	private MessageFormat isDescendantNodeFormatter = new MessageFormat("ISDESCENDANTNODE(pageContent, ''{0}'')");

	private MessageFormat brightcoveVideoQueryFormatter = new MessageFormat(
			"SELECT parent.* FROM [dam:AssetContent] AS parent INNER JOIN [nt:base] AS child ON ISCHILDNODE(child,parent) WHERE ISDESCENDANTNODE(parent, ''{0}'') AND child.[applicationTag] = ''{1}''");

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private transient Externalizer externalizer;

	@Reference
	private transient GenericListService genericListService;

	@Activate
	public void activate(AssetXmlFeedConfigs config) {
		this.assetTypes = config.assetTypes();
		this.pagePaths = config.pagePaths();
		this.brightcoveVideoPaths = config.brightcoveVideoPaths();
		this.applicationTagId = config.applicationTagId();
		this.isExtensionlessUrl = config.extensionlessUrl();
		this.thumbnailListPath = config.thumbnailListPath();
	}

	@Override
	public void writeAssets(XMLStreamWriter stream, Resource assetFolder, SlingHttpServletRequest request,
			String externalizerDomainName, Map<String, String> metadataMap, Map<String, String> tagMetadataMap, Map<String,String> configMap)
			throws XMLStreamException, ParseException {
		LOG.debug("Inside AssetXmlFeedServiceImpl class");
		for (Iterator<Resource> children = assetFolder.listChildren(); children.hasNext();) {
			Resource assetFolderChild = children.next();
			if (assetFolderChild.isResourceType(DamConstants.NT_DAM_ASSET)) {
				Asset asset = assetFolderChild.adaptTo(Asset.class);

				Resource resource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT)
						.getChild(DAM_METADATA);
				ValueMap properties = resource.getValueMap();
				String fileFormat = StringUtils.EMPTY;

				String assetType = properties.get(DamConstants.DC_FORMAT, String.class);
				if (assetType != null && StringUtils.isNotEmpty(assetType)) {
					String[] format = assetType.split("/");
					if (format.length > 1) {
						fileFormat = format[1];
					}
				} else {
					assetType = properties.get("dam:MIMEtype", String.class);
					if (assetType != null && StringUtils.isNotEmpty(assetType)) {
						String[] format = assetType.split("/");
						if (format.length > 1) {
							fileFormat = format[1];
						}
					} else {
						fileFormat = properties.get("dam:Fileformat", String.class);
					}
				}

				if (isAssetFormat(fileFormat) && isCrawlable(asset,configMap)) {
					writeAsset(asset, stream, request, externalizerDomainName, metadataMap, tagMetadataMap,configMap.get("useInternalDomain"));
				}

			} else {
				writeAssets(stream, assetFolderChild, request, externalizerDomainName, metadataMap, tagMetadataMap,configMap);
			}
		}

	}

	private boolean isCrawlable(Asset asset,Map<String, String> configMap) {
		boolean isCrawlable = true;		
		String includeOnlyTaggedAsset = configMap.get("includeOnlyTaggedAsset");
		if (StringUtils.equalsIgnoreCase("TRUE", includeOnlyTaggedAsset)) {
			Resource contentResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT)
					.getChild(DAM_METADATA);
			if (contentResource != null) {
				ValueMap properties = contentResource.getValueMap();
				String applicationTag = properties.get("applicationTag", String.class);
				if (!StringUtils.equalsIgnoreCase(applicationTag, this.applicationTagId)) {
					isCrawlable = false;
				}
			}

		}

		return isCrawlable;
	}

	@Override
	public void writePageData(XMLStreamWriter stream, SlingHttpServletRequest request, String externalizerDomainName,
			Map<String, String> metadataMap, Map<String, String> tagMetadataMap, Map<String,String> configMap) throws XMLStreamException {
		String pagePaths = configMap.get("pagePaths");
		if (StringUtils.isNotEmpty(pagePaths)) {
			String[] pathArray = pagePaths.split(",");
			if (ArrayUtils.isNotEmpty(pathArray)) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String path : pathArray) {
					Object[] argsPath = { path };
					sb.append(isDescendantNodeFormatter.format(argsPath));
					i++;
					if (i < pathArray.length) {
						sb.append(" OR ");
					}
				}
				Object[] args = { sb.toString() };
				String jcr2Query = this.pageQueryFormatter.format(args);
				LOG.info("Page fetch query : {} ", jcr2Query);
				try (ResourceResolver resolver = resourceResolverFactory
						.getResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
					Iterator<Resource> result = resolver.findResources(jcr2Query, Query.JCR_SQL2);

					while (result != null && result.hasNext()) {
						Resource jcrContentResource = result.next();
						String pagePath = jcrContentResource.getParent().getPath();
						stream.writeStartElement(NS, "url");
						String loc = externalizePageUri(request, pagePath, externalizerDomainName,configMap.get("useInternalDomain"));
						writeElement(stream, "loc", loc);
						if (jcrContentResource != null) {
							ValueMap properties = jcrContentResource.getValueMap();
							writeElement(stream, "eTitle", properties.get("jcr:title",String.class));
							writeElement(stream, "eDescription", properties.get("jcr:description",String.class));
							writeElement(stream, "eSubtitle", properties.get("subtitle",String.class));
							writeElement(stream, "eReadTime", properties.get("readTime",String.class));							
							writeImagePropertyValue(stream, "eImage", "image", properties);
							writeDate(stream,"ePublishedDate","publishDate",jcrContentResource);

							for (Map.Entry<String, String> item : tagMetadataMap.entrySet()) {
								writeTagPropertyValue(stream, item.getKey(), item.getValue(), properties,
										request.getResourceResolver());
							}

						}
						stream.writeEndElement();
					}

				} catch (LoginException | ParseException e) {
					LOG.error("Error getting resource resolver", e);
				}
			}
		}
	}


	@Override
	public void writeBrightcoveVideoData(XMLStreamWriter stream, SlingHttpServletRequest request,
			String externalizerDomainName, Map<String, String> metadataMap, Map<String, String> tagMetadataMap,Map<String,String> configMap)
			throws XMLStreamException, ParseException {
		if (ArrayUtils.isNotEmpty(this.brightcoveVideoPaths) && StringUtils.isNotBlank(applicationTagId)) {
			for (String path : this.brightcoveVideoPaths) {
				Object[] args = { path, this.applicationTagId };
				String jcr2Query = this.brightcoveVideoQueryFormatter.format(args);
				try (ResourceResolver resolver = resourceResolverFactory
						.getResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
					Iterator<Resource> result = resolver.findResources(jcr2Query, Query.JCR_SQL2);
					while (result != null && result.hasNext()) {
						Resource assetContentResource = result.next();
						Resource assetResource = assetContentResource.getParent();
						Asset asset = assetResource.adaptTo(Asset.class);
						writeAsset(asset, stream, request, externalizerDomainName, metadataMap, tagMetadataMap,configMap.get("useInternalDomain"));
					}
				} catch (LoginException e) {
					LOG.error("Error getting resource resolver", e);
				}
			}
		}
	}
	
	private boolean isAssetFormat(String assetType) {
		for (String type : assetTypes) {
			if (type.equalsIgnoreCase(assetType)) {
				return true;
			}
		}
		return false;
	}
	
	private void writeDate(XMLStreamWriter stream, String elementName, String propertyName,
			Resource jcrContentResource) throws XMLStreamException, ParseException {
		ValueMap properties = jcrContentResource.getValueMap();
		String value = properties.get(propertyName,String.class);
		String jcrLastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, String.class);
		if (value != null && StringUtils.isNotEmpty(value)) {
			Date lastPublishedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value);
			long epoch = lastPublishedDate.getTime() / 1000;
			writeElement(stream, elementName, Long.toString(epoch));
		} else if (jcrLastModified != null) {			
				Date modifiedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(jcrLastModified);
				long epoch = modifiedDate.getTime() / 1000;
				writeElement(stream, elementName, Long.toString(epoch));
			} else {
				String jcrCreated = properties.get(JcrConstants.JCR_CREATED, String.class);
				Date createdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(jcrCreated);
				long epoch = createdDate.getTime() / 1000;
				writeElement(stream, elementName, Long.toString(epoch));
			}
		
	}

	private void writeAsset(Asset asset, XMLStreamWriter stream, SlingHttpServletRequest request,
			String externalizerDomainName, Map<String, String> metadataMap, Map<String, String> tagMetadataMap,String useInternalDomain)
			throws XMLStreamException, ParseException {
		stream.writeStartElement(NS, "url");
		String loc = externalizeUri(request, asset.getPath(), externalizerDomainName, useInternalDomain);
		writeElement(stream, "loc", loc);

		Resource contentResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT)
				.getChild(DAM_METADATA);
		if (contentResource != null) {
			ValueMap properties = contentResource.getValueMap();
			writeFirstPropertyValue(stream, "filetype", DamConstants.DC_FORMAT, properties, asset);
			writeFirstPropertyValue(stream, "filesize", DamConstants.DAM_SIZE, properties, asset);
			writeImagePropertyValue(stream, "eImage", DamConstants.DC_FORMAT, properties);
			for (Map.Entry<String, String> item : metadataMap.entrySet()) {
				writeFirstPropertyValue(stream, item.getKey(), item.getValue(), properties, asset);
			}

			for (Map.Entry<String, String> item : tagMetadataMap.entrySet()) {
				writeTagPropertyValue(stream, item.getKey(), item.getValue(), properties,
						request.getResourceResolver());
			}

		}

		stream.writeEndElement();
	}

	private void writeImagePropertyValue(XMLStreamWriter stream, String elementName, String propertyName,
			ValueMap properties) throws XMLStreamException {
		String value = properties.get(propertyName, String.class);
		Map<String, String> thumbnailMap = genericListService.getGenericListAsMap(this.thumbnailListPath);
		if (MapUtils.isNotEmpty(thumbnailMap) && StringUtils.isNotBlank(value)) {
			String imgPath = thumbnailMap.get(value);
			if (StringUtils.isEmpty(imgPath)) {
				imgPath = thumbnailMap.get("default");
			}
			if (StringUtils.isNotBlank(imgPath)) {
				writeElement(stream, elementName, imgPath);
			}
		}
	}

	private String externalizeUri(SlingHttpServletRequest request, String path, String externalizerDomainName, String useInternalDomain) {
		String mappedPath = request.getResourceResolver().map(request, path);
		if(StringUtils.equalsIgnoreCase("TRUE", useInternalDomain)) {
			return externalizer.externalLink(request.getResourceResolver(), INTERNAL_DOMAIN, mappedPath);
		}
		return externalizer.externalLink(request.getResourceResolver(), externalizerDomainName, mappedPath);
	}

	private String externalizePageUri(SlingHttpServletRequest request, String path, String externalizerDomainName, String useInternalDomain) {
		String mappedPath = request.getResourceResolver().map(request, path);
		if (!this.isExtensionlessUrl) {
			mappedPath = mappedPath + ".html";
		}
		if(StringUtils.equalsIgnoreCase("TRUE", useInternalDomain)) {
			return externalizer.externalLink(request.getResourceResolver(), INTERNAL_DOMAIN, mappedPath);
		}
		return externalizer.externalLink(request.getResourceResolver(), externalizerDomainName, mappedPath);
	}

	private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName,
			final String propertyName, final ValueMap properties, Asset asset)
			throws XMLStreamException, ParseException {

		String value = properties.get(propertyName, String.class);

		if (value != null && propertyName.equals("dam:size")) {
			double size = Double.parseDouble(value) / 1024;
			DecimalFormat df = new DecimalFormat("0.00");
			String assetSize = df.format(size) + " KB";
			writeElement(stream, elementName, assetSize);
		} else if (propertyName.equals(DamConstants.DC_FORMAT)) {
			if (value != null) {
				String[] format = value.split("/");
				writeElement(stream, elementName, format[1]);
			} else {
				writeElement(stream, elementName, getFileFormat(asset));
			}
		} else if (propertyName.equals("publishDate")) {
			if (value != null && StringUtils.isNotEmpty(value)) {
				Date lastPublishedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value);
				long epoch = lastPublishedDate.getTime() / 1000;
				writeElement(stream, elementName, Long.toString(epoch));
			} else {
				writeElement(stream, elementName, getCreatedOrModifiedDate(asset));
			}
		} else if (value != null) {
			writeElement(stream, elementName, value);
		}

	}

	private String getFileFormat(Asset asset) throws ParseException {
		Resource metaRes = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT).getChild(DAM_METADATA);
		ValueMap vm = metaRes.getValueMap();
		String fileType = vm.get("dam:MIMEtype", String.class);
		if (fileType != null && StringUtils.isNotEmpty(fileType)) {
			String[] format = fileType.split("/");
			if (format.length > 1) {
				return format[1];
			}

		} else {
			return vm.get("dam:Fileformat", String.class);
		}
		return StringUtils.EMPTY;
	}

	private String getCreatedOrModifiedDate(Asset asset) throws ParseException {
		Resource metaResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT).getChild(DAM_METADATA);
		ValueMap vm = metaResource.getValueMap();
		String jcrLastModified = vm.get(JcrConstants.JCR_LASTMODIFIED, String.class);

		if (jcrLastModified != null) {
			Date modifiedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(jcrLastModified);
			long epoch = modifiedDate.getTime() / 1000;
			return Long.toString(epoch);
		} else {
			Resource contentResource = asset.adaptTo(Resource.class);
			ValueMap properties = contentResource.getValueMap();
			String jcrCreated = properties.get(JcrConstants.JCR_CREATED, String.class);
			Date createdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(jcrCreated);
			long epoch = createdDate.getTime() / 1000;
			return Long.toString(epoch);
		}
	}

	private void writeTagPropertyValue(final XMLStreamWriter stream, final String elementName,
			final String propertyName, final ValueMap properties, ResourceResolver resourceResolver)
			throws XMLStreamException {
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		String[] tagArray = properties.get(propertyName, String[].class);
		if (null != tagArray && tagArray.length > 0) {

			for (String tag : tagArray) {
				Tag eTag = tagManager.resolve(tag);
				if (null != eTag) {
					writeElement(stream, elementName, eTag.getTitle());
				}
			}
		}
	}

	private void writeElement(final XMLStreamWriter stream, final String elementName, final String text)
			throws XMLStreamException {
		if(StringUtils.isNotBlank(text)) {
			stream.writeStartElement(NS, elementName);
			stream.writeCharacters(text);
			stream.writeEndElement();
		}		
	}

}
