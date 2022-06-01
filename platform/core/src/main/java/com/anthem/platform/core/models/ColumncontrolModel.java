package com.anthem.platform.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling model for column control Component.
 *
 * @author hima naga uday kiran
 */

@Model(adaptables = {Resource.class}, resourceType = "anthem/platform/components/master/columncontrol/v1/columncontrol", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class ColumncontrolModel{

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ColumncontrolModel.class);

	/**
	 * Value to number Of Columns
	 */
	@ValueMapValue
	private int[] numOfColumns;
	
	List<Integer> parsysList = new ArrayList<>();
	
	@PostConstruct
	protected void init() {
		LOG.info("inside column control");
	}

	public int[] getNumOfColumns() {
		if(null != numOfColumns && numOfColumns.length > 0 ) {
			return numOfColumns.clone();
		}
		return null;
	}
	

	public List<Integer> getParsysList() {
		parsysList = IntStream.of(numOfColumns)
        .boxed().collect(Collectors.toCollection(ArrayList::new));
		return new ArrayList<> (parsysList);
	}
	
}