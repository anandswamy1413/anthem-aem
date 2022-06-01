package com.anthem.platform.core.services.utility.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.services.utility.ContentFragmentJsonService;

@SuppressWarnings("squid:S2221")
@Component(service = ContentFragmentJsonService.class, immediate = true)
public class ContentFragmentJsonServiceImpl implements ContentFragmentJsonService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentJsonServiceImpl.class);
	static final String CF_PATH = "/jcr:content/data/";

	@Override
	public String contentFragmentToJson(Resource resource, String contentFragmentName, ResourceResolver resolver) {
		LOG.info("Inside CF Service");
		String name;
		String content;
		JSONObject jsonObject = new JSONObject();


		if (contentFragmentName != null) {
			LOG.info("Content Fragment Name {}", contentFragmentName);
			Resource resourceContent = resolver.getResource(resource.getPath() + CF_PATH + contentFragmentName);
			if (resourceContent != null) {
				ValueMap valueMap = resourceContent.getValueMap();

				Iterator<Map.Entry<String, Object>> itr = valueMap.entrySet().iterator();

				while (itr.hasNext()) {
					Entry<String, Object> set = itr.next();
					name = set.getKey();
					if (set.getValue() != null && set.getValue() instanceof String[]) {
						String[] valueArray = (String[]) set.getValue();
						try {
							JSONArray jsonArray = new JSONArray();
							for (String loop : valueArray) {
								jsonArray.put(loop);
							}
							jsonObject.put(name, jsonArray);
						} catch (JSONException e) {
							LOG.error("JSONException in ContentFragmentServiceImpl", e);
						}
					} else
						try {
							content = (String) set.getValue();
							if (StringUtils.isNotEmpty(content)) {
								jsonObject.put(name, content);
							}
						} catch (JSONException e) {
							LOG.error("JSONException in ContentFragmentServiceImpl", e);
						} catch (ClassCastException e) {
							LOG.error("ClassCastException in ContentFragmentServiceImpl", e);
						}
				}
			}

		}
		return jsonObject.toString();
	}
}
