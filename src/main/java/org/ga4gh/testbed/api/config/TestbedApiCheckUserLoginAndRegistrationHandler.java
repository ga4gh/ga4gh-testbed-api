package org.ga4gh.testbed.api.config;

import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.testbed.api.model.GithubUser;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Configuration
public class TestbedApiCheckUserLoginAndRegistrationHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private LoggingUtil loggingUtil;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // check our database for the current user's github id
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = token.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String userGithubId = (String) attributes.get("login");
        GithubUser dbGithubUser = hibernateUtil.readEntityObject(GithubUser.class, userGithubId, false);

        // If the user tries to access any secure page, they will be redirected to /login landing page
        // If the user is not in the database, then add them to the database and redirect to the home page
        if (dbGithubUser==null){
            // create GithubUserOrganization object
            GithubUser githubUser = new GithubUser();
            githubUser.setGithubId(userGithubId);
            try {
                hibernateUtil.createEntityObject(GithubUser.class,githubUser);
                loggingUtil.debug(String.format("Added github user : %s to the database",userGithubId));
            } catch (EntityExistsException e) {
                loggingUtil.error(String.format("Error adding the user to the database. %s",e.getMessage()));
            }
        }
        // TODO: redirect to the app's homepage.
        redirectStrategy.sendRedirect(request, response, "/reports");
    }
}