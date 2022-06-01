package com.anthem.platform.core.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;

public class PlatformConstants {
	
	private PlatformConstants() {
		super();
	}
	
	public static final String 	LOCATION = "loc";
	public static final String 	LAST_MODIFIED = "lastmodified";
	public static final String 	SUB_SERVICE_NAME = "datareadwrite";
	public static final String FORWARD_SLASH = "/";
	public static final String PUBLISH_RUN_MODE = "publish";
	public static final String HTTPS = "https";
	public static final String HTTP = "http";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String APIKEY = "apikey";
	public static final String SCOPE = "scope";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String GRANT_TYPE = "grant_type";
	public static final String CLIENT_CREDS = "client_credentials";
	public static final String PUBLIC = "public";
	public static final String BASIC_SPACE = "Basic ";
	public static final String BEARER_SPACE = "Bearer ";
	public static final String FLU = "flu";
	public static final String INC = "inc";
	public static final String COM = "com";
	public static final String EMPTY_JSON_OBJECT = "{}";
	/** The Constant HTML_SUFFIX containing html suffix. */
	public static final String HTML_SUFFIX = ".html";
	public static final String PDF_SUFFIX = ".pdf";
	public static final String ZIP_SUFFIX = ".zip";
	public static final String JSON_SUFFIX = ".json";

	/** The Constant DAM_CONTENT path prefix. */
	public static final String DAM_CONTENT = "/content/dam";
	public static final String HTTPS_COLON = "https://";
	public static final Map<String, Object> ANTHEM_AUTO_INFO;
	public static final String AUTHOR = "author";
	public static final String PDF_MIME_TYPE = "application/pdf";
	public static final String PATH = "path";
	public static final String PATH_FLAT = "path.flat";
	public static final String P_LIMIT = "p.limit";
	public static final String MINUS_ONE = "-1";
	public static final String TYPE = "type";
	public static final String PROPERTY = "property";
	public static final String PROPERTY_VALUE = "property.value";
	public static final String NODE_NAME = "nodename";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String IPHEN = "-";
	public static final String DOT = ".";
	public static final String EQUAL_TO = "=";
	public static final String ROOT = "root";
	public static final String DATA = "data";

	public static final String TEMP_DIR_PATH = "/tmp/";
	public static final String INTERNAL_DOMAINS = "internal-domains";
	public static final String TFN_SET = "tfnSet";
	public static final String CF_PATH = "/jcr:content/data/";
	
	static {
		Map<String,Object> infoMap = new HashMap<>();
		infoMap.put(ResourceResolverFactory.SUBSERVICE, PlatformConstants.SUB_SERVICE_NAME);
		ANTHEM_AUTO_INFO = Collections.unmodifiableMap(infoMap);
	}
	
}        
