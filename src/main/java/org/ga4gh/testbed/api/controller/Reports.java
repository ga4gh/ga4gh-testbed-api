package org.ga4gh.testbed.api.controller;

import org.ga4gh.testbed.api.model.Report;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class Reports {

    @GetMapping(path = "/{reportId:.+}")
    public Report getReport(
        @PathVariable String reportId
    ) {
        Report report = new Report();
        report.setId(reportId);
        return report;
    }

    @PostMapping()
    public Report postReport(
        @RequestBody Report report
    ) {
        return report;
    }
}
