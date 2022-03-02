package org.ga4gh.testbed.api.config;

import org.ga4gh.testbed.api.utils.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TestbedApiSavedRequestAwareAuthenticationSuccessHandler testbedApiSavedRequestAwareAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // every request is authenticated using oauth2.0
        http.authorizeRequests().anyRequest().authenticated().and().oauth2Login().successHandler(testbedApiSavedRequestAwareAuthenticationSuccessHandler);
    }

}