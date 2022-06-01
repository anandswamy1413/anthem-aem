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
 * Sling model for grid Component.
 *
 * @author Amit Rathor
 */

@Model(adaptables = {Resource.class}, resourceType = "anthem/platform/components/master/web-components/grid/v1/grid", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json", options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })
public class GridModel{

	/**
	 * Standard logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(GridModel.class);

	/**
	 * Value to number Of Grids
	 */
	@ValueMapValue
	private int[] numberOfGrids;
	
	List<Integer> columnList = new ArrayList<>();
	
	@PostConstruct
	protected void init() {
		LOG.info("inside grid component");
	}
	

	public List<Integer> getParsysList() {
		columnList = IntStream.of(numberOfGrids)
        .boxed().collect(Collectors.toCollection(ArrayList::new));
		return columnList;
	}
	
}