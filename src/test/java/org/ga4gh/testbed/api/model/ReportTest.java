package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReportTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "01d0e947-5975-4786-a755-5025fec7416d",
                "ga4gh-testbed-report",
                "0.1.0",
                new HashMap<String, String>() {{
                    put("url", "https://testsite.ga4gh.org/api");
                }},
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.PASS,
                new Summary() {{
                    setId(Long.valueOf(0));
                    setPassed(10);
                }},
                new ArrayList<Phase>() {{
                    add(new Phase() {{
                        setPhaseName("sequence");
                    }});
                }},
                new ReportSeries() {{
                    setId("1edb5213-52a2-434f-a7b8-b101fea8fb30");
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testReport(String id, String schemaName, String schemaVersion,
        Map<String, String> inputParameters, LocalDateTime startTime,
        LocalDateTime endTime, Status status, Summary summary, List<Phase> phases,
        ReportSeries reportSeries) {

        Report report = new Report();
        report.loadRelations();

        report.setId(id);
        report.setSchemaName(schemaName);
        report.setSchemaVersion(schemaVersion);
        report.setInputParameters(inputParameters);
        report.setStartTime(startTime);
        report.setEndTime(endTime);
        report.setStatus(status);
        report.setSummary(summary);
        report.setPhases(phases);
        report.setReportSeries(reportSeries);

        Assert.assertEquals(report.getId(), id);
        Assert.assertEquals(report.getSchemaName(), schemaName);
        Assert.assertEquals(report.getSchemaVersion(), schemaVersion);
        Assert.assertEquals(report.getInputParameters(), inputParameters);
        Assert.assertEquals(report.getStartTime(), startTime);
        Assert.assertEquals(report.getEndTime(), endTime);
        Assert.assertEquals(report.getStatus(), status);
        Assert.assertEquals(report.getSummary(), summary);
        Assert.assertEquals(report.getPhases(), phases);
        Assert.assertEquals(report.getReportSeries(), reportSeries);
    }
}
