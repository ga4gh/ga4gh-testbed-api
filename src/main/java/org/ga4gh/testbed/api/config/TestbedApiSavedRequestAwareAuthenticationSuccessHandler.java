package org.ga4gh.testbed.api.config;

import org.checkerframework.checker.units.qual.*;
import org.springframework.context.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.client.authentication.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.savedrequest.*;
import org.springframework.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@Configuration
public class TestbedApiSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = token.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String githubId = (String) attributes.get("login");
        // check if user exists db

        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            this.requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

}
