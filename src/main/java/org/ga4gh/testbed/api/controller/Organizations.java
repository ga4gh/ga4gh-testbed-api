package org.ga4gh.testbed.api.controller;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Organization;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizations")
public class Organizations {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private BasicShowRequestHandler<String, Organization> showOrganization;

    @GetMapping
    @JsonView(SerializeView.OrganizationSimple.class)
    public List<Organization> getOrganizations() {
        return hibernateUtil.listEntityObject(Organization.class);
    }

    @GetMapping(path = "/{organizationId:.+}")
    @JsonView(SerializeView.OrganizationFull.class)
    public Organization getOrganization(
        @PathVariable String organizationId
    ) {
        return showOrganization.prepare(organizationId).handleRequest();
    }
}
