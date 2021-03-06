package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestbedTestTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(0),
                "test_sequence_circular",
                "Test to check if server passes all the edge cases related to circular queries",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.PASS,
                new Summary() {{
                    setId(Long.valueOf(0));
                    setPassed(10);
                }},
                new ArrayList<TestbedCase>() {{
                    add(new TestbedCase() {{
                        setId(Long.valueOf(0));
                        setMessage("log message 1");
                    }});
                }},
                new Phase() {{
                    setId(Long.valueOf(0));
                    setStatus(Status.PASS);
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testTestbedTest(Long id, String testName, String testDescription,
        LocalDateTime startTime, LocalDateTime endTime, Status status,
        Summary summary, List<TestbedCase> cases, Phase phase) {

        TestbedTest testbedTest = new TestbedTest();
        testbedTest.loadRelations();

        testbedTest.setId(id);
        testbedTest.setTestName(testName);
        testbedTest.setTestDescription(testDescription);
        testbedTest.setStartTime(startTime);
        testbedTest.setEndTime(endTime);
        testbedTest.setStatus(status);
        testbedTest.setSummary(summary);
        testbedTest.setCases(cases);
        testbedTest.setPhase(phase);

        Assert.assertEquals(testbedTest.getId(), id);
        Assert.assertEquals(testbedTest.getTestName(), testName);
        Assert.assertEquals(testbedTest.getTestDescription(), testDescription);
        Assert.assertEquals(testbedTest.getStartTime(), startTime);
        Assert.assertEquals(testbedTest.getEndTime(), endTime);
        Assert.assertEquals(testbedTest.getStatus(), status);
        Assert.assertEquals(testbedTest.getSummary(), summary);
        Assert.assertEquals(testbedTest.getCases(), cases);
        Assert.assertEquals(testbedTest.getPhase(), phase);
    }
}
