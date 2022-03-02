package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.annotation.*;
import org.ga4gh.testbed.api.model.*;
import org.ga4gh.testbed.api.utils.*;
import org.ga4gh.testbed.api.utils.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.*;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.web.authentication.preauth.x509.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@RestController
public class AuthenticationController {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @GetMapping(path = "/")
    @JsonView(SerializeView.TestbedFull.class)
    public String getHomePage() {
        return "Welcome to the Home Page!!!!";
    }

    @GetMapping(path = "/login")
    @JsonView(SerializeView.TestbedFull.class)
    public String getLogin() {
        // take you to github login page
        // lets you login using OAuth
        //
        // if login fail -> error page with 401
        // if login pass -> check registered = check if user exists in database
        // no -> error page asking to register
        // yes -> redirects to home page: "/"
        return "You have successfully logged in!!!!";
    }

    @GetMapping(path = "/register")
    @JsonView(SerializeView.TestbedFull.class)
    public String registerUser() {
        return "You have successfully register!!!!";
    }

    @GetMapping(path = "/loginSuccess")
    @JsonView(SerializeView.TestbedFull.class)
    public String loginSuccess() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = token.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String githubId = (String) attributes.get("login");
        return githubId + " - login success" ;

    }

    @GetMapping(path = "/loginFailure")
    @JsonView(SerializeView.TestbedFull.class)
    public String loginFailure() {
        return "login fail!!!!";
    }

}
