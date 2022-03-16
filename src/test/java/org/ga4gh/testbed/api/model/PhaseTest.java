package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PhaseTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(0),
                "sequence",
                "refget sequence endpoint",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.PASS,
                new Summary() {{
                    setId(Long.valueOf(0));
                    setPassed(10);
                }},
                new ArrayList<TestbedTest>() {{
                    add(new TestbedTest() {{
                        setId(Long.valueOf(0));
                        setTestName("test_sequence_circular");
                    }});
                }},
                new Report() {{
                    setId("01d0e947-5975-4786-a755-5025fec7416d");
                    setStatus(Status.PASS);
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testPhase(Long id, String phaseName, String phaseDescription,
        LocalDateTime startTime, LocalDateTime endTime, Status status,
        Summary summary, List<TestbedTest> tests, Report report) {

        Phase phase = new Phase();
        phase.loadRelations();

        phase.setId(id);
        phase.setPhaseName(phaseName);
        phase.setPhaseDescription(phaseDescription);
        phase.setStartTime(startTime);
        phase.setEndTime(endTime);
        phase.setStatus(status);
        phase.setSummary(summary);
        phase.setTests(tests);
        phase.setReport(report);

        Assert.assertEquals(phase.getId(), id);
        Assert.assertEquals(phase.getPhaseName(), phaseName);
        Assert.assertEquals(phase.getPhaseDescription(), phaseDescription);
        Assert.assertEquals(phase.getStartTime(), startTime);
        Assert.assertEquals(phase.getEndTime(), endTime);
        Assert.assertEquals(phase.getStatus(), status);
        Assert.assertEquals(phase.getSummary(), summary);
        Assert.assertEquals(phase.getTests(), tests);
        Assert.assertEquals(phase.getReport(), report);
    }
}
