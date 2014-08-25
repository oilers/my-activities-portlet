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

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
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
 
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		request.getPreferences().setValue("testParam", "This is the song that never ends");
		Integer logins = -1;
		Integer blogs = -1;
		
		try {
			
			JournalArticle rightArticle = JournalArticleServiceUtil.getArticle(10184L, "11461");
			if(rightArticle != null) {
				String content = rightArticle.getContent();
				DateFormat format = new SimpleDateFormat("MM/dd/YYYY");
				User user = PortalUtil.getUser(request);
				logins = (Integer) user.getExpandoBridge().getAttribute("logins");
				request.getPreferences().setValue("logins", logins.toString());
				blogs = (Integer) user.getExpandoBridge().getAttribute("blogs");
				request.getPreferences().setValue("blogs", logins.toString());
				content = content.replace("[FULL_NAME]", user.getFullName())
						.replace("[TODAYS_DATE]", format.format(new Date()))
						.replace("[NUM_LOGINS]", logins.toString())
						.replace("[NUM_CREATED_BLOGS]", blogs.toString())
						.replace("]]>", "");
				request.getPreferences().setValue("content", content);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		super.render(request, response);
	}
	
	
}
