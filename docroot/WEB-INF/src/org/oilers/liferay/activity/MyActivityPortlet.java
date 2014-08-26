package org.oilers.liferay.activity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.service.BlogsEntryServiceUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleService;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleFinderUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticleUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class MyActivityPortlet
 */
public class MyActivityPortlet extends MVCPortlet {
 
	private static final String TEMPLATE_ARTICLE_ID = "11461";
	private static final long TEMPLATE_GROUP_ID = 10184L;
	private static final String LOGIN_PROPERTY = "logins";

	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		request.getPreferences().setValue("testParam", "This is the song that never ends");
		Integer logins = -1;
		
		try {
			JournalArticle rightArticle = JournalArticleServiceUtil.getArticle(TEMPLATE_GROUP_ID, TEMPLATE_ARTICLE_ID);
			if(rightArticle != null) {
				String content = rightArticle.getContent();
				DateFormat format = new SimpleDateFormat("MM/dd/YYYY");
				User user = PortalUtil.getUser(request);
				DynamicQuery dynamicQuery = BlogsEntryLocalServiceUtil.dynamicQuery();
				dynamicQuery.add(PropertyFactoryUtil.forName("userId").eq(user.getUserId()));
				Long blogs = BlogsEntryLocalServiceUtil.dynamicQueryCount(dynamicQuery);
				ExpandoBridge expandoBridge = user.getExpandoBridge();
				if(!user.getExpandoBridge().hasAttribute(LOGIN_PROPERTY)) {
					user.getExpandoBridge().addAttribute(LOGIN_PROPERTY, ExpandoColumnConstants.INTEGER, 0);
				}
				logins = (Integer) expandoBridge.getAttribute(LOGIN_PROPERTY);
				content = content.replace("[FULL_NAME]", user.getFullName())
						.replace("[TODAYS_DATE]", format.format(new Date()))
						.replace("[NUM_LOGINS]", logins.toString())
						.replace("[NUM_CREATED_BLOGS]", blogs + "")
						.replace("]]>", "");
				request.getPreferences().setValue("content", content);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		super.render(request, response);
	}
	
	
}
