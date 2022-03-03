package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationController {

    // TODO: Replace these placeholder endpoints with actual endpoints

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @GetMapping(path = "/")
    @JsonView(SerializeView.TestbedFull.class)
    public String testBedApiHomePage() {
        return "Testbed API Home Page!!!!";
    }

    @GetMapping(path = "/register")
    @JsonView(SerializeView.TestbedFull.class)
    public String registerPage() {
        return "Landing Page for register / Login";
    }

    @GetMapping(path = "/login")
    @JsonView(SerializeView.TestbedFull.class)
    public String loginPage() {
        return "Landing Page for register / Login";
    }

    @GetMapping(path = "/oauthregister")
    @JsonView(SerializeView.TestbedFull.class)
    public String oauthRegisterUser() {
        return "endpoint for oauth2 register";
    }

    @GetMapping(path = "/oauthlogin")
    @JsonView(SerializeView.TestbedFull.class)
    public String oauthLoginUser() {
        return "endpoint for oauth2 login";
    }

    @GetMapping(path = "/loginFailure")
    @JsonView(SerializeView.TestbedFull.class)
    public String loginFailure(@RequestParam(required = false) String error) {
        return "error: " + error;
    }

    @GetMapping(path = "/registerFailure")
    @JsonView(SerializeView.TestbedFull.class)
    public String registerFailure(@RequestParam(required = false) String error) {
        return "error: " + error;
    }

}
