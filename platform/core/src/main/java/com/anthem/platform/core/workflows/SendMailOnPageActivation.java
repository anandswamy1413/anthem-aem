package com.anthem.platform.core.workflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.commons.Externalizer;
import com.day.cq.dam.core.process.CommandLineProcess;
import com.day.crx.JcrConstants;

@Component(immediate = true, service = WorkflowProcess.class, property = {
		"process.label=" + "Send Mail on Page Activation - Anthem" })
public class SendMailOnPageActivation implements WorkflowProcess {

	private static final Logger LOG = LoggerFactory.getLogger(SendMailOnPageActivation.class);

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private EmailService emailService;

	@Reference
	private Externalizer externalizer;

	public static final String PAGE_PATH = "etc/anthem/compare-to-current-page.html";
	public static final String REQUEST_FOR_ACTIVATION_DIFFURL = "/etc/notification/email/anthem/request-for-activation-diffurl.txt";
	public static final String REQUEST_FOR_ACTIVATION = "/etc/notification/email/anthem/request-for-activation.txt";
	public static final String EMAIL_PROPERTY = "./profile/email";
	public static final String INBOX = "aem/inbox";

	
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		String processArgs = metaDataMap.get(CommandLineProcess.Arguments.PROCESS_ARGS.name(), String.class);
		String payload = workItem.getWorkflowData().getPayload().toString();
		if (StringUtils.isNotEmpty(processArgs)) {
			try (ResourceResolver resourceResolver = resourceResolverFactory
					.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
				Session session = resourceResolver.adaptTo(Session.class);
				if (session != null) {
					VersionManager vm = session.getWorkspace().getVersionManager();
					Node payloadNode = resourceResolver.resolve(payload + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT).adaptTo(Node.class);
					if (payloadNode.hasProperty(JcrConstants.JCR_BASEVERSION)) {
						if (vm != null) {
							Version baseVersion = vm.getBaseVersion(payload + PlatformConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT);
							if (baseVersion != null) {
								sendActivationMail(processArgs, resourceResolver, baseVersion.getIdentifier(), payload,
										REQUEST_FOR_ACTIVATION_DIFFURL);
							}

						}
					} else {
						sendActivationMail(processArgs, resourceResolver, StringUtils.EMPTY, payload,
								REQUEST_FOR_ACTIVATION);
					}
				}
			} catch (LoginException e) {
				LOG.error("LoginException getting service resource resolver ", e);
			} catch (RepositoryException e) {
				LOG.error("RepositoryException getting service resource resolver ", e);
			}
		}

	}

	private void sendActivationMail(String processArgs, ResourceResolver resourceResolver, String versionId,
			String payload, String template) throws RepositoryException {

		UserManager userManager = resourceResolver.adaptTo(UserManager.class);
		Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
		String domainName = externalizer.externalLink(resourceResolver, PlatformConstants.AUTHOR, "");

		String compareUrl = domainName + PAGE_PATH + "?" + "vid=" + versionId + "&" + "path=" + payload;
		String pageUrl = domainName + payload.substring(1) + PlatformConstants.HTML_SUFFIX;
		String inboxUrl = domainName + INBOX;
		
		if (StringUtils.isNotEmpty(domainName) && StringUtils.isNotEmpty(payload)){
			// Set the dynamic variables of your email template
			Map<String, String> emailParams = new HashMap<String, String>();
			emailParams.put("compareUrl", compareUrl);
			emailParams.put("pageUrl", pageUrl);
			emailParams.put("inboxUrl", inboxUrl);

			// Customize the sender email address - if required
			emailParams.put("senderEmailAddress", "aempublishingnotifications@anthem.com");
			emailParams.put("senderName", "AEM Notification");

			// List of email recipients
			List<String> recipients = new ArrayList<String>();
			Authorizable authorizable = userManager.getAuthorizable(processArgs);
			if (authorizable != null && authorizable.isGroup()) {
				org.apache.jackrabbit.api.security.user.Group group = (org.apache.jackrabbit.api.security.user.Group) authorizable;
				Iterator<Authorizable> itr = group.getMembers();
				while (itr.hasNext()) {
					Object obj = itr.next();
					if (obj instanceof User) {
						User user = (User) obj;
						if (user != null) {
							Authorizable userAuthorization = userManager.getAuthorizable(user.getID());
							if (userAuthorization.hasProperty(EMAIL_PROPERTY)
									&& user.getProperty(EMAIL_PROPERTY).length > 0) {
								recipients.add(user.getProperty(EMAIL_PROPERTY)[0].toString());
							}
						}
					}
				}
			}

			String[] recipientsArray = new String[recipients.size()];
			recipientsArray = recipients.toArray(recipientsArray);
			LOG.debug("RecipientsList:{}", recipients.toString());

			// emailService.sendEmail(..) returns a list of all the recipients that could not be sent the email
			// An empty list indicates 100% success
			List<String> failureList = emailService.sendEmail(template, emailParams, recipientsArray);

			if (failureList.isEmpty()) {
				LOG.debug("Email sent successfully to the recipients");
			} else {
				LOG.debug("Email sent failed");
			}
		}
	}

}
