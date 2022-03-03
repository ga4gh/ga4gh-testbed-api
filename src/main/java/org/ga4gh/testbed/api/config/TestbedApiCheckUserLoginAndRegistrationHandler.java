package org.ga4gh.testbed.api.config;

import org.ga4gh.starterkit.common.hibernate.exception.EntityExistsException;
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
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Configuration
public class TestbedApiCheckUserLoginAndRegistrationHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {


        // Get the request's target url
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) savedRequest;
        String accessedURI = defaultSavedRequest.getRequestURI();

        // check our database for the current user's github id
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = token.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String userGithubId = (String) attributes.get("login");
        GithubUser dbGithubUser = hibernateUtil.readEntityObject(GithubUser.class, userGithubId, false);


        // TODO: invalidate the access token and delete it (logout logic - but it requires FE to provide the CSRF token?!)
        // If user is not registered, render the login page with error message


        // ideally accessURI will be either "/oauthregister" or "/oauthlogin".
        // If the user tries to access any secure page, they will be redirected to /login landing page
        // and will be forced to choose either "/oauthregister" or "/oauthlogin".

        if (accessedURI.equals("/oauthregister")){
            // user is trying to register
            if (dbGithubUser==null){
                // create GithubUserOrganization object
                GithubUser githubUser = new GithubUser();
                githubUser.setGithubId(userGithubId);

                try {
                    hibernateUtil.createEntityObject(GithubUser.class,githubUser);
                    redirectStrategy.sendRedirect(request, response, "/");
                } catch (EntityExistsException e) {
                    e.printStackTrace();
                }
            }
            else {
                redirectStrategy.sendRedirect(request, response, "/registerFailure?error=user_already_exists");
            }
        }
        else {
            // user is trying to login
            if (dbGithubUser == null) {
                redirectStrategy.sendRedirect(request, response, "/loginFailure?error=user_is_not_registered");
            }
            else {
                redirectStrategy.sendRedirect(request, response, "/");
            }

        }
    }

}