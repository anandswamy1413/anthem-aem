package com.anthem.platform.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.crx.JcrConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(immediate = true, service = PlatformFileHelper.class)
public class PlatformFileHelper {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformFileHelper.class);

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	public InputStream getFileInputStream(String filePath, ResourceResolver resourceResolver) {
		Resource fileResource = resourceResolver.getResource(filePath);
		if (fileResource != null) {
			LOG.info("fileResource {}", fileResource.getName());
			Asset asset = fileResource.adaptTo(Asset.class);
			Resource original = asset.getOriginal();
			return original.adaptTo(InputStream.class);
		}
		return null;
	}

	public Resource getRenditionResource(String absPath, String renditionName, ResourceResolver resourceResolver) {

		Resource resource = resourceResolver.getResource(absPath);
		if (DamUtil.isAsset(resource)) {
			Asset asset = resource.adaptTo(Asset.class);
			Rendition rendition = null;
			if (StringUtils.isEmpty(renditionName)) {
				rendition = asset.getOriginal();
			} else {
				rendition = asset.getRendition(renditionName);
			}
			if (null != rendition) {
				resource = rendition.adaptTo(Resource.class);
			}
		}
		return resource;

	}

	public String readjcrDataAsString(String absPath, ResourceResolver resourceResolver) {
		String dataString = null;
		try {
			Property dataProperty = readjcrData(absPath, resourceResolver);
			if (dataProperty != null) {
				InputStream is = dataProperty.getBinary().getStream();
				dataString = IOUtils.toString(is, StandardCharsets.UTF_8.name());
			}
		} catch (IOException | RepositoryException e) {
			LOG.error("Exception reading Data as String ", e);
		}
		return dataString;
	}

	public Property readjcrData(String absPath, ResourceResolver resourceResolver) {
		Node ntResourceNode = null;
		Property nodeData = null;
		try {
			String resourcePath = absPath + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT;
			LOG.info("Getting resource:{}", resourcePath);
			Resource resource = resourceResolver.getResource(resourcePath);

			if (resource != null) {
				LOG.info("Got resource:{}", resource.getPath());
				ntResourceNode = resource.adaptTo(Node.class);
				// Not checking for null here since the resource is already available
				nodeData = ntResourceNode.getProperty(JcrConstants.JCR_DATA);
			}
		} catch (RepositoryException e) {
			LOG.error("Exception reading Data ", e);
		}
		return nodeData;
	}

	public Document readjcrDataAsXML(String absPath, ResourceResolver resourceResolver) {
		Document dataXML = null;
		try {
			Property dataProperty = readjcrData(absPath, resourceResolver);
			if (dataProperty != null) {
				InputStream is = dataProperty.getBinary().getStream();
				dataXML = Jsoup.parse(is, StandardCharsets.UTF_8.displayName(), absPath, Parser.xmlParser());
			}
		} catch (RepositoryException | IOException e) {
			LOG.error("Exception reading Data as XML", e);
		}
		return dataXML;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> readDataAsJson(String absPath, ResourceResolver resourceResolver) {
		try {
			Property dataProperty = readjcrData(absPath, resourceResolver);
			if (dataProperty != null) {
				InputStream is = dataProperty.getBinary().getStream();
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(is, Map.class);
			}
		} catch (RepositoryException | IOException e) {
			LOG.error("Exception reading Data as JSON", e);
		}
		return null;
	}

	public Asset getAsset(String filePath, ResourceResolver resourceResolver) {
		Resource fileResource = resourceResolver.getResource(filePath);
		if (fileResource != null) {
			LOG.info("fileResource {}", fileResource.getName());
			Asset asset = fileResource.adaptTo(Asset.class);
			return asset;
		}
		return null;
	}

	public InputStream getOriginalInputStream(Asset asset) {
		Resource original = asset.getOriginal();
		return original.adaptTo(InputStream.class);
	}

	public File createZipFiles(List<File> files, String fileName) {
		try {
			File tempDir = new File(PlatformConstants.TEMP_DIR_PATH);
			File zipFile = new File(tempDir, FilenameUtils.getName(FilenameUtils.getName(fileName.concat(PlatformConstants.ZIP_SUFFIX))));

			try (OutputStream fos = new FileOutputStream(zipFile);) {
				try (ZipOutputStream zipOut = new ZipOutputStream(fos)) {
					for (File fileToZip : files) {
						try (FileInputStream fis = new FileInputStream(fileToZip)) {
							ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
							zipOut.putNextEntry(zipEntry);
							IOUtils.copy(fis, zipOut);
						}
					}
				}
				zipFile.deleteOnExit();
				tempDir.deleteOnExit();
				return zipFile;
			}
		} catch (IOException e) {
			LOG.error("Exception while creating zip files {} ", e);
		}
		return null;
	}

}
