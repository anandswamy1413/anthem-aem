package com.anthem.platform.core.workflows;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.acs.commons.email.EmailService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;

class SendMailOnPageActivationTest {

	@InjectMocks
	private SendMailOnPageActivation mailOnPageActivation;
	
	@Mock
	ResourceResolverFactory resourceResolverFactory;

	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	private WorkItem workItem;
	
	@Mock
	private WorkflowSession workflowSession;
	
	@Mock
	private WorkflowData workflowData;
	
	@Mock
	private Session session;
	
	@Mock
	private Resource assetResource;
	
	@Mock
	private Workspace workspace;
	
	@Mock
	private VersionManager versionMgr;
	
	@Mock
	private Version baseVersion;
	
	@Mock
	private Version predecessorVersion;
	
	@Mock
	private Externalizer externalizer;
	
	@Mock
	private UserManager userManager;

	@Mock
	private Authorizable authorizable;
	
	@Mock
	private EmailService emailService;
	
	@Mock
	Node payloadNode;
	
	@Mock
	Iterator<Authorizable> itr;
	
	@Mock
	org.apache.jackrabbit.api.security.user.Group group;
	
	private List<String> failureList = new ArrayList<>();
	
	Map<String, String> emailParams = new HashMap<String, String>();
	
	private MetaDataMap metaDataMap;
	
	private String payload = "/content/test";
	
	String[] recipientsArray = {"aemtestuser@xyz.com"};
	
	@BeforeEach
	public void setup() throws  LoginException, RepositoryException {
		MockitoAnnotations.initMocks(this);
		metaDataMap = new SimpleMetaDataMap();
		emailParams.put("compareUrl", "/test/compare/url");
		emailParams.put("inboxUrl", "/aem/inbox/url");
		when(workItem.getWorkflowData()).thenReturn(workflowData);
		when(workflowData.getPayload()).thenReturn("/content/test");
		when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(resourceResolver);
		when(workflowData.getMetaDataMap()).thenReturn(metaDataMap);
		when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)).thenReturn(resourceResolver);
		when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
		when(session.getWorkspace()).thenReturn(workspace);
		when(session.getWorkspace().getVersionManager()).thenReturn(versionMgr);
		when(resourceResolver.resolve(payload + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT)).thenReturn(assetResource);
		when(assetResource.adaptTo(Node.class)).thenReturn(payloadNode);
	}
	@Test
	void testExecute() throws WorkflowException, LoginException, RepositoryException {
		when(payloadNode.hasProperty(JcrConstants.JCR_BASEVERSION)).thenReturn(true);
		when(versionMgr.getBaseVersion(payload + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT)).thenReturn(baseVersion);
		when(baseVersion.getLinearPredecessor()).thenReturn(predecessorVersion);
		when(predecessorVersion.getIdentifier()).thenReturn("uniqueid");
		when(resourceResolver.adaptTo(UserManager.class)).thenReturn(userManager);
		when(resourceResolver.adaptTo(Externalizer.class)).thenReturn(externalizer);
		when(externalizer.externalLink(resourceResolver, PlatformConstants.AUTHOR, "")).thenReturn("localhost");
		when(userManager.getAuthorizable("anthem-send-group")).thenReturn(authorizable);
		when(emailService.sendEmail("/etc/sample/template", emailParams, recipientsArray)).thenReturn(failureList);
		when(resourceResolver.resolve("/content/test/jcr:content")).thenReturn(assetResource);
		metaDataMap.put("PROCESS_ARGS", "anthem-send-group");
		mailOnPageActivation.execute(workItem, workflowSession, metaDataMap);
	}
	
	@Test
	void testExecuteElse() throws RepositoryException, WorkflowException, LoginException {
		when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)).thenThrow(new LoginException());
		mailOnPageActivation.execute(workItem, workflowSession, metaDataMap);
	    when(payloadNode.hasProperty(JcrConstants.JCR_BASEVERSION)).thenThrow(new RepositoryException());
	    mailOnPageActivation.execute(workItem, workflowSession, metaDataMap);
		when(session.getWorkspace().getVersionManager()).thenReturn(versionMgr);
		mailOnPageActivation.execute(workItem, workflowSession, metaDataMap);
		failureList.add(payload);
		when(emailService.sendEmail("/etc/sample/template", emailParams, recipientsArray)).thenReturn(failureList);
		mailOnPageActivation.execute(workItem, workflowSession, metaDataMap);
	}
	
	
}
