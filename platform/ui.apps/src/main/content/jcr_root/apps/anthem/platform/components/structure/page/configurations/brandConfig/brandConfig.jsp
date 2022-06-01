<%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false"
          import="org.apache.sling.api.resource.Resource,
                  com.day.cq.i18n.I18n,
                  java.util.Map, com.day.cq.commons.jcr.JcrConstants, org.apache.commons.lang.StringUtils" %>
<%@taglib prefix="ui" uri="http://www.adobe.com/taglibs/granite/ui/1.0"%>

<ui:includeClientLib categories="page.brandClientLibs.properties" />
<% response.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains"); %>

<%
	final I18n i18n = new I18n(slingRequest);
	String contentPath = (String) request.getAttribute("granite.ui.form.contentpath");
	final Resource pageRes = resourceResolver.getResource(contentPath);

	boolean brndInherited = Boolean.FALSE;
	String rootPage = StringUtils.EMPTY;
	String brndJs[];
	String brndCss[];

try{
	int depth = pageRes.getParent().adaptTo(Page.class).getDepth();
	String[] brndJsPath =pageRes.getValueMap().get("jsClientLibs", String[].class);
	String[] brndCssPath =pageRes.getValueMap().get("cssClientLibs", String[].class);
	Page parentPage = pageRes.getParent().adaptTo(Page.class);


	if(brndJsPath == null && brndCssPath == null){
       for(int i = depth; i>=3; i--){

        brndJs = parentPage.adaptTo(Resource.class).getChild("jcr:content").getValueMap().get("jsClientLibs", String[].class);
		brndCss = parentPage.adaptTo(Resource.class).getChild("jcr:content").getValueMap().get("cssClientLibs", String[].class);

           if(brndJs == null && brndCss == null){
           		parentPage = parentPage.getParent().adaptTo(Page.class);
          	} else {
				rootPage = parentPage.getPath();
            	break;
   			 }
   		 }
	}

	brndInherited = (brndJsPath == null && brndCssPath == null) && !rootPage.equals("");

} catch(Exception e) {}

%>


<section class="coral-Form-fieldset">
  <%
    if (brndInherited) {
        %><coral-checkbox class="inherited-brand-clientLibs" <%= brndInherited ? "checked" : "" %>>
            <%= xssAPI.encodeForHTML(i18n.get("Inherited from {0}", "Checkbox label", rootPage)) %>
        </coral-checkbox><%
    }
%>
</section>



