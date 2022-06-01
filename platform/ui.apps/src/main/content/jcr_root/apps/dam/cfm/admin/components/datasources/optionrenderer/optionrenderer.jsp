<%--
  ADOBE CONFIDENTIAL

  Copyright 2017 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="org.apache.commons.collections.Transformer,
                  org.apache.commons.collections.iterators.TransformIterator,
                  com.adobe.granite.ui.components.ds.DataSource,
                  com.adobe.granite.ui.components.ds.SimpleDataSource,
                  org.apache.sling.api.resource.ResourceResolver,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.api.wrappers.ValueMapDecorator,
                  java.util.HashMap,
                  com.adobe.granite.ui.components.ds.ValueMapResource,
                  org.apache.sling.api.resource.ResourceMetadata,
                  java.util.List,
                  java.util.ArrayList,
                  java.util.Arrays" %><%

    final ResourceResolver resolver = resourceResolver;
    ValueMap props = resource.getValueMap();
    String optionsStr = props.get("options", "");
    String[] optionsArray = optionsStr.split(",");
    List<String> options = new ArrayList<String>(4);
    options.addAll(Arrays.asList(optionsArray));

    @SuppressWarnings("unchecked")
    DataSource ds = new SimpleDataSource(new TransformIterator(options.iterator(), new Transformer() {
        public Object transform(Object input) {
            try {
                String text = ((String) input).trim();
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
				String[] txtArray = text.split(";");
                vm.put("value", txtArray[1]);
                vm.put("text", txtArray[0]);

                return new ValueMapResource(
                        resolver, new ResourceMetadata(), "nt:unstructured", vm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }));

    request.setAttribute(DataSource.class.getName(), ds);

%>