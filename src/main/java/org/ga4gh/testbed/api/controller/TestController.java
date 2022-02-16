package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.annotation.JsonView;

import org.ga4gh.testbed.api.model.Specification;
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

    @GetMapping(path = "/specification")
    @JsonView(SerializeView.SpecificationFull.class)
    public Specification getExampleSpecification() {
        return hibernateUtil.readEntityObject(Specification.class, "refget", true);
    }

    @GetMapping(path = "/testbed")
    @JsonView(SerializeView.TestbedFull.class)
    public Testbed getExampleTestbed() {
        return hibernateUtil.readEntityObject(Testbed.class, "refget-compliance", true);
    }

    
}
