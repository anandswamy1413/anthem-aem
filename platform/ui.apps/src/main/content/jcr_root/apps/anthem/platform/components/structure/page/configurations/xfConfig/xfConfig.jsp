<%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false"
          import="org.apache.sling.api.resource.Resource,
                  com.day.cq.i18n.I18n,
                  java.util.Map, com.day.cq.commons.jcr.JcrConstants, org.apache.commons.lang.StringUtils" %>
<%@taglib prefix="ui" uri="http://www.adobe.com/taglibs/granite/ui/1.0"%>

<ui:includeClientLib categories="page.headerfooter.properties" />
<% response.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains"); %>

<%
	final I18n i18n = new I18n(slingRequest);
	String contentPath = (String) request.getAttribute("granite.ui.form.contentpath");
	final Resource pageRes = resourceResolver.getResource(contentPath);

	boolean inherited = Boolean.FALSE;
	String rootPage = StringUtils.EMPTY;
	String XfHeader = StringUtils.EMPTY;
	String XfFooter = StringUtils.EMPTY;

try{
	int depth = pageRes.getParent().adaptTo(Page.class).getDepth();
	String headerFragmentPath =pageRes.getValueMap().get("headerFragmentPath", String.class);
    String footerFragmentPath =pageRes.getValueMap().get("footerFragmentPath", String.class);
    Page parentPage = pageRes.getParent().adaptTo(Page.class);


	if(headerFragmentPath == null && footerFragmentPath == null){
       for(int i = depth; i>=3; i--){

        XfHeader = parentPage.adaptTo(Resource.class).getChild("jcr:content").getValueMap().get("headerFragmentPath", String.class);
		XfFooter = parentPage.adaptTo(Resource.class).getChild("jcr:content").getValueMap().get("footerFragmentPath", String.class);

           if(XfHeader == null && XfFooter == null){
           		parentPage = parentPage.getParent().adaptTo(Page.class);
          	} else {
				rootPage = parentPage.getPath();
            	break;
   			 }
   		 }
	}

	 inherited = (headerFragmentPath == null && footerFragmentPath == null) && !rootPage.equals("");

} catch(Exception e) {}

%>


<section class="coral-Form-fieldset">
  <%
    if (inherited) {
        %><coral-checkbox class="inherited-header-footer" <%= inherited ? "checked" : "" %>>
            <%= xssAPI.encodeForHTML(i18n.get("Inherited from {0}", "Checkbox label", rootPage)) %>
        </coral-checkbox><%
    }
%>
</section>



