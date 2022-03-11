package org.ga4gh.testbed.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TestbedApiCheckUserLoginAndRegistrationHandler testbedApiCheckUserLoginAndRegistrationHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // every request is authenticated using oauth2.0
        http
                .authorizeRequests()
                .antMatchers("/test/*", "/reports/*" )
                .authenticated()
                .and()
                .oauth2Login()
                .successHandler(testbedApiCheckUserLoginAndRegistrationHandler);
    }
}