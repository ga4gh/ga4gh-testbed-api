package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class TestbedVersionTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                Integer.valueOf(0),
                "1.2.6",
                new Testbed() {{
                    setId("refget");
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testTestbedVersion(Integer id, String version, Testbed testbed) {

        TestbedVersion testbedVersion = new TestbedVersion();
        testbedVersion.loadRelations();

        testbedVersion.setId(id);
        testbedVersion.setTestbedVersion(version);
        testbedVersion.setTestbed(testbed);

        Assert.assertEquals(testbedVersion.getId(), id);
        Assert.assertEquals(testbedVersion.getTestbedVersion(), version);
        Assert.assertEquals(testbedVersion.getTestbed(), testbed);
    }
}