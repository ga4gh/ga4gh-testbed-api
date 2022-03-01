package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.testbed.api.model.GithubUser;
import org.ga4gh.testbed.api.model.Organization;
import org.ga4gh.testbed.api.model.Platform;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.model.ReportSeries;
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

    @GetMapping(path = "/platform")
    @JsonView(SerializeView.PlatformFull.class)
    public Platform getExamplePlatform() {
        return hibernateUtil.readEntityObject(Platform.class, "org.ga4gh.refget.starterkit", true);
    }

    @GetMapping(path = "/organization")
    @JsonView(SerializeView.OrganizationSecure.class)
    public Organization getExampleOrganization() {
        return hibernateUtil.readEntityObject(Organization.class, "org.ga4gh", true);
    }

    @GetMapping(path = "/github-user")
    @JsonView(SerializeView.GithubUserSecure.class)
    public GithubUser getExampleGithubUser() {
        return hibernateUtil.readEntityObject(GithubUser.class, "ga4gh-user", true);
    }

    @GetMapping(path = "/report-series")
    @JsonView(SerializeView.ReportSeriesFull.class)
    public ReportSeries getExampleReportSeries() {
        return hibernateUtil.readEntityObject(ReportSeries.class, "1edb5213-52a2-434f-a7b8-b101fea8fb30", true);
    }

    @GetMapping(path = "/report")
    @JsonView(SerializeView.ReportFull.class)
    public Report getExampleReport() {
        return hibernateUtil.readEntityObject(Report.class, "01d0e947-5975-4786-a755-5025fec7416d", true);
    }
}
