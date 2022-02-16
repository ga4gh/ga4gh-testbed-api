package org.ga4gh.testbed.api.controller;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.Testbed;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @GetMapping(path = "/testbed")
    @JsonView(SerializeView.TestbedSimple.class)
    public Testbed testController() {
        try {
            Testbed testbed = hibernateUtil.readEntityObject(Testbed.class, "refget-compliance", false);
            System.out.println(testbed.getId());
            System.out.println(testbed.getTestbedName());
            System.out.println(testbed.getTestbedDescription());
            System.out.println(testbed.getRepoUrl());
            System.out.println(testbed.getDockerhubUrl());
            System.out.println(testbed.getDockstoreUrl());
            
            // ObjectMapper mapper = new ObjectMapper();
            // System.out.println(mapper.writeValueAsString(testbed));
            return testbed;
        } catch (Exception ex) {
            System.out.println("Error retrieving testbed");
            System.out.println(ex);
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
