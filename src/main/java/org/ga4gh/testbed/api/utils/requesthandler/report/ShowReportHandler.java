package org.ga4gh.testbed.api.utils.requesthandler.report;

import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.utils.DTO.ReportCountDTO;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            throw new ResourceNotFoundException("No Report exists at id '" + id + "'");
        }
        return report;
    }

    public List<ReportCountDTO> getSubmittedReportsCount() {
        List<Report> reports = getHiberateUtil().listEntityObject(Report.class);
        Map<String, Integer> reportCounts = new HashMap<>();

        for (Report report : reports) {
            reportCounts.put(report.getTestbed().getId(), reportCounts.getOrDefault(report.getTestbed().getId(), 0) + 1);
        }

        List<ReportCountDTO> reportCountDTOs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : reportCounts.entrySet()) {
            ReportCountDTO reportCountDTO = new ReportCountDTO(entry.getKey(), entry.getValue());
            reportCountDTOs.add(reportCountDTO);
        }

        return reportCountDTOs;
    }

    public Map<String, List<Report>> getReportsByTestbed(List<Report> reports) {
        Map<String, List<Report>> newReports = groupReports(reports);
        return newReports;
    }

    private Map<String, List<Report>> groupReports(List<Report> reports) {
        return reports.
                stream().
                collect(Collectors.groupingBy(report -> report.getTestbed().getId()));
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
