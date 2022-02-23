package org.ga4gh.testbed.api.controller;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.ga4gh.testbed.api.utils.requesthandler.report.CreateReportHandler;
import org.ga4gh.testbed.api.utils.requesthandler.report.ShowReportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class Reports {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private ShowReportHandler showReport;

    @Autowired
    private CreateReportHandler createReport;

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
    @JsonView(SerializeView.ReportFull.class)
    public Report postReport(
        @RequestHeader("GA4GH-TestbedReportSeriesId") String reportSeriesId,
        @RequestHeader("GA4GH-TestbedReportSeriesToken") String reportSeriesToken,
        @RequestBody Report report
    ) throws Exception {
        return createReport.prepare(reportSeriesId, reportSeriesToken, report).handleRequest();
    }
}
