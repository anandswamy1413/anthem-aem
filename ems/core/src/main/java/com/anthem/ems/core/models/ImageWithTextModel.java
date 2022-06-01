/**
 * 
 */
package com.anthem.ems.core.models;

import com.anthem.platform.core.models.AbstractImageComponentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Map;

/**
 * This is the model class for Image with Text Component. We have properties in
 * the General tab, Text dialogue and we are extending
 * AbstractImageComponentModel for image dialogue properties.
 * 
 * 
 * @author Prakhar Bhatt
 * @since Aug 18, 2021
 */

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, resourceType = ImageWithTextModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageWithTextModel extends AbstractImageComponentModel {

	protected static final String RESOURCE_TYPE = "/apps/ems/components/content/ImageWithText";

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String text;

	@ValueMapValue
	private boolean textAlignment;

	@ValueMapValue
	private boolean bgColor;

	@ValueMapValue
	private boolean dynamicAccessText;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the richText
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the textAlignment
	 */
	public boolean getTextAlignment() {
		return textAlignment;
	}

	/**
	 * @return the bgColor
	 */
	public boolean getBgColor() {
		return bgColor;
	}

	/**
	 * @return the dynamicAccessText
	 */
	public boolean getDynamicAccessText() {
		return dynamicAccessText;
	}

	public Map<String, String> getImageMap() {
		return super.getImageMap();
	}

}
