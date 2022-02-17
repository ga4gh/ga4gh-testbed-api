package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.annotation.*;
import org.ga4gh.testbed.api.model.*;
import org.ga4gh.testbed.api.utils.*;
import org.ga4gh.testbed.api.utils.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestSecurity {

        @GetMapping(path = "/helloworld")
        public String getExampleSpecification() {
        return "Hello World!!!!!!!!!";
        }
    }