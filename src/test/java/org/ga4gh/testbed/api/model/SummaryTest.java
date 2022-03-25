package org.ga4gh.testbed.api.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SummaryTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(0),
                1,
                2,
                3,
                4,
                5,
                new Report() {{
                    setId("01d0e947-5975-4786-a755-5025fec7416d");
                }},
                null,
                null
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testSummary(Long id, Integer unknown, Integer passed,
        Integer warned, Integer failed, Integer skipped, Report report,
        Phase phase, TestbedTest testbedTest) {

        Summary summary = new Summary();
        summary.loadRelations();

        summary.setId(id);
        summary.setUnknown(unknown);
        summary.setPassed(passed);
        summary.setWarned(warned);
        summary.setFailed(failed);
        summary.setSkipped(skipped);
        summary.setReport(report);
        summary.setPhase(phase);
        summary.setTestbedTest(testbedTest);

        Assert.assertEquals(summary.getId(), id);
        Assert.assertEquals(summary.getUnknown(), unknown);
        Assert.assertEquals(summary.getPassed(), passed);
        Assert.assertEquals(summary.getWarned(), warned);
        Assert.assertEquals(summary.getFailed(), failed);
        Assert.assertEquals(summary.getSkipped(), skipped);
        Assert.assertEquals(summary.getReport(), report);
        Assert.assertEquals(summary.getPhase(), phase);
        Assert.assertEquals(summary.getTestbedTest(), testbedTest);
    }
}
