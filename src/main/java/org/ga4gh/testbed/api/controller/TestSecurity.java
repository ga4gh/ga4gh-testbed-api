package org.ga4gh.testbed.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecurity {

        @GetMapping(path = "/helloworld")
        public String getExampleSpecification() {
        return "Hello World!!!!!!!!!";
        }
    }