package com.anthem.platform.core.models;

import java.util.List;
import java.util.Map;

import com.anthem.platform.core.models.impl.TitleCtaDescriptionImpl.IconList;

public interface TitleCtaDescription {
	public List<IconList> getCtaList();

	public String getCtaLinks();

	public String getId();

	public String getTitleField();

	public String getType();

	public String getTitleFontWeight();

	public String getTitleLetterSpacing();

	public String getTitleTextColor();

	public String getLinkURL();

	public String getDescRequired();

	public String getEnableBluebar();

	public String getDescription();

	public String getDescriptionWeight();

	public String getDescTextColor();

	public String getLinkTextColor();
	
	public Map<String,String> getImageMap();
}
