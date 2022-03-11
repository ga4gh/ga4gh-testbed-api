package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PlatformTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "org.ga4gh.refget.starterkit",
                "Refget Starter Kit",
                "GA4GH Starter Kit deployment of Refget specification",
                new Organization() {{
                    setId("org.ga4gh");
                }},
                new ArrayList<Specification>() {{
                    add(new Specification() {{
                        setId("refget");
                    }});
                }},
                new ArrayList<ReportSeries>() {{
                    add(new ReportSeries() {{
                        setId("1edb5213-52a2-434f-a7b8-b101fea8fb30");
                    }});
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testPlatform(String id, String platformName, String platformDescription,
        Organization organization, List<Specification> specifications,
        List<ReportSeries> reportSeries) {

        Platform platform = new Platform();
        platform.loadRelations();

        platform.setId(id);
        platform.setPlatformName(platformName);
        platform.setPlatformDescription(platformDescription);
        platform.setOrganization(organization);
        platform.setSpecifications(specifications);
        platform.setReportSeries(reportSeries);

        Assert.assertEquals(platform.getId(), id);
        Assert.assertEquals(platform.getPlatformName(), platformName);
        Assert.assertEquals(platform.getPlatformDescription(), platformDescription);
        Assert.assertEquals(platform.getOrganization(), organization);
        Assert.assertEquals(platform.getSpecifications(), specifications);
        Assert.assertEquals(platform.getReportSeries(), reportSeries);
    }
}
