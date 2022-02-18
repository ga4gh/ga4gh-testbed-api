package org.ga4gh.testbed.api.utils.requesthandler.report;

import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ShowReportHandler extends BasicShowRequestHandler<String, Report> {

    private String id;

    @Autowired
    TestbedApiHibernateUtil hibernateUtil;

    public ShowReportHandler() {
        super(Report.class);
    }

    public ShowReportHandler prepare(String id) {
        setId(id);
        return this;
    }

    public Report handleRequest() {
        Report report = getHiberateUtil().readFullReport(getId());
        if (report == null) {
            throw new ResourceNotFoundException("No Report exists at id " + id);
        }
        return report;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setHibernateUtil(TestbedApiHibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public TestbedApiHibernateUtil getHiberateUtil() {
        return hibernateUtil;
    }
}
