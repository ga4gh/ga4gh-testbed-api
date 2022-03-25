package org.ga4gh.testbed.api.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LogMessageTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Long.valueOf(0),
                "An example log message",
                new TestbedCase() {{
                    setId(Long.valueOf(100));
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testLogMessage(Long id, String message, TestbedCase testbedCase) {

        LogMessage logMessage = new LogMessage();
        logMessage.loadRelations();

        logMessage.setId(id);
        logMessage.setMessage(message);
        logMessage.setTestbedCase(testbedCase);

        Assert.assertEquals(logMessage.getId(), id);
        Assert.assertEquals(logMessage.getMessage(), message);
        Assert.assertEquals(logMessage.getTestbedCase(), testbedCase);
    }
}
