package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class TestbedTestNG {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "refget-compliance",
                "Refget Compliance Suite",
                "Test compliance of Refget services to specification",
                "https://github.com/ga4gh/refget-compliance-suite",
                "https://hub.docker.com/repository/docker/ga4gh/refget-compliance-suite",
                "https://dockstore.org/containers/registry.hub.docker.com/ga4gh/refget-compliance-suite",
                new ArrayList<Specification>() {{
                    add(new Specification() {{
                        setId("refget");
                    }});
                }},
                new ArrayList<ReportSeries>() {{
                    add(new ReportSeries() {{
                        setId("1edb5213-52a2-434f-a7b8-b101fea8fb30");
                    }});
                }},
                new ArrayList<TestbedVersion>() {{
                    add(new TestbedVersion() {{
                        setId(0);
                        setTestbedVersion("1.2.6");
                    }});
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testTestbed(String id, String testbedName, String testbedDescription,
        String repoUrl, String dockerhubUrl, String dockstoreUrl, 
        List<Specification> specifications, List<ReportSeries> reportSeries,
        List<TestbedVersion> testbedVersions) {

        Testbed testbed = new Testbed();
        testbed.loadRelations();

        testbed.setId(id);
        testbed.setTestbedName(testbedName);
        testbed.setTestbedDescription(testbedDescription);
        testbed.setRepoUrl(repoUrl);
        testbed.setDockerhubUrl(dockerhubUrl);
        testbed.setDockstoreUrl(dockstoreUrl);
        testbed.setSpecifications(specifications);
        testbed.setReportSeries(reportSeries);
        testbed.setTestbedVersions(testbedVersions);

        Assert.assertEquals(testbed.getId(), id);
        Assert.assertEquals(testbed.getTestbedName(), testbedName);
        Assert.assertEquals(testbed.getTestbedDescription(), testbedDescription);
        Assert.assertEquals(testbed.getRepoUrl(), repoUrl);
        Assert.assertEquals(testbed.getDockerhubUrl(), dockerhubUrl);
        Assert.assertEquals(testbed.getDockstoreUrl(), dockstoreUrl);
        Assert.assertEquals(testbed.getSpecifications(), specifications);
        Assert.assertEquals(testbed.getReportSeries(), reportSeries);
        Assert.assertEquals(testbed.getTestbedVersions(), testbedVersions);
    }
}