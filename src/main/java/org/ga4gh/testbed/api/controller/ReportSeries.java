package org.ga4gh.testbed.api.controller;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report-series")
public class ReportSeries {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private BasicShowRequestHandler<String, org.ga4gh.testbed.api.model.ReportSeries> showReportSeries;

    @GetMapping
    @JsonView(SerializeView.ReportSeriesSimple.class)
    public List<org.ga4gh.testbed.api.model.ReportSeries> getAllReportSeries() {
        return hibernateUtil.listEntityObject(org.ga4gh.testbed.api.model.ReportSeries.class);
    }

    @GetMapping(path = "/{reportSeriesId:.+}")
    @JsonView(SerializeView.ReportSeriesFull.class)
    public org.ga4gh.testbed.api.model.ReportSeries getSingleReportSeries(
        @PathVariable String reportSeriesId
    ) {
        return showReportSeries.prepare(reportSeriesId).handleRequest();
    }
}
