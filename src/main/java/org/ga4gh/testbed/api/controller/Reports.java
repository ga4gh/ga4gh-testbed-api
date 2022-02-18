package org.ga4gh.testbed.api.controller;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.ga4gh.testbed.api.utils.requesthandler.report.ShowReportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class Reports {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private ShowReportHandler showReport;

    @GetMapping
    @JsonView(SerializeView.ReportSimple.class)
    public List<Report> getReports() {
        return hibernateUtil.listEntityObject(Report.class);
    }

    @GetMapping(path = "/{reportId:.+}")
    @JsonView(SerializeView.ReportFull.class)
    public Report getReport(
        @PathVariable String reportId
    ) {
        return showReport.prepare(reportId).handleRequest();
    }

    @PostMapping
    public Report postReport(@RequestBody Report report) throws Exception {
        try {
            report.setId(UUID.randomUUID().toString());
            hibernateUtil.createEntityObject(Report.class, report);
            return hibernateUtil.readFullReport(report.getId());
        } catch (Exception ex) {
            System.out.println("Exception");
            System.out.println(ex);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
        
    }
}
