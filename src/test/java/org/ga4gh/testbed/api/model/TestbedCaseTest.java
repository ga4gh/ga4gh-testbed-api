package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestbedCaseTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(0),
                "case 1",
                "test for seq 1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.PASS,
                "server responds as expected",
                new ArrayList<String>() {{
                    add("log message 1");
                }},
                new TestbedTest() {{
                    setId(Long.valueOf(0));
                    setStatus(Status.PASS);
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testTestbedCase(Long id, String caseName, String caseDescription,
        LocalDateTime startTime, LocalDateTime endTime, Status status,
        String message, List<String> logMessages, TestbedTest testbedTest) {

        TestbedCase testbedCase = new TestbedCase();
        testbedCase.loadRelations();

        testbedCase.setId(id);
        testbedCase.setCaseName(caseName);
        testbedCase.setCaseDescription(caseDescription);
        testbedCase.setStartTime(startTime);
        testbedCase.setEndTime(endTime);
        testbedCase.setStatus(status);
        testbedCase.setMessage(message);
        testbedCase.setLogMessages(logMessages);
        testbedCase.setTestbedTest(testbedTest);

        Assert.assertEquals(testbedCase.getId(), id);
        Assert.assertEquals(testbedCase.getCaseName(), caseName);
        Assert.assertEquals(testbedCase.getCaseDescription(), caseDescription);
        Assert.assertEquals(testbedCase.getStartTime(), startTime);
        Assert.assertEquals(testbedCase.getEndTime(), endTime);
        Assert.assertEquals(testbedCase.getStatus(), status);
        Assert.assertEquals(testbedCase.getMessage(), message);
        Assert.assertEquals(testbedCase.getLogMessages(), logMessages);
        Assert.assertEquals(testbedCase.getTestbedTest(), testbedTest);
    }
}
