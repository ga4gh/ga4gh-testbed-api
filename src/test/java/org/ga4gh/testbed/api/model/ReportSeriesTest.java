package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ReportSeriesTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "1edb5213-52a2-434f-a7b8-b101fea8fb30",
                "abc",
                "def",
                new Testbed() {{
                    setId("refget-compliance");
                }},
                new Platform() {{
                    setId("org.ga4gh.refget.starterkit");
                }},
                new ArrayList<Report>() {{
                    add(new Report() {{
                        setId("01d0e947-5975-4786-a755-5025fec7416d");
                    }});
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testReportSeries(String id, String tokenSalt, String tokenHash,
        Testbed testbed, Platform platform, List<Report> reports) {

        ReportSeries reportSeries = new ReportSeries();
        reportSeries.loadRelations();

        reportSeries.setId(id);
        reportSeries.setTokenSalt(tokenSalt);
        reportSeries.setTokenHash(tokenHash);
        reportSeries.setTestbed(testbed);
        reportSeries.setPlatform(platform);
        reportSeries.setReports(reports);

        Assert.assertEquals(reportSeries.getId(), id);
        Assert.assertEquals(reportSeries.getTokenSalt(), tokenSalt);
        Assert.assertEquals(reportSeries.getTokenHash(), tokenHash);
        Assert.assertEquals(reportSeries.getTestbed(), testbed);
        Assert.assertEquals(reportSeries.getPlatform(), platform);
        Assert.assertEquals(reportSeries.getReports(), reports);
    }
}
