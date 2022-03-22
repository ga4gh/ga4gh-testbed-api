package org.ga4gh.testbed.api.controller;

import org.ga4gh.testbed.api.app.TestbedApi;
import org.ga4gh.testbed.api.app.TestbedApiSpringConfig;
import org.ga4gh.testbed.api.testutils.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
@ContextConfiguration(classes = {
    TestbedApi.class,
    TestbedApiSpringConfig.class,
    ReportSeries.class
})
@WebAppConfiguration
public class ReportSeriesTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private static final String RESPONSE_DIR = "/responses/report-series/";

    @DataProvider(name = "getReportSeriesCases")
    public Object[][] getReportSeriesCases() {
        return new Object[][] {
            {
                "1edb5213-52a2-434f-a7b8-b101fea8fb30",
                "00",
                true,
                200
            },
            {
                "483382e9-f92b-466d-9427-154d56a75fcf",
                "01",
                true,
                200
            },
            {
                "00000000-0000-0000-0000-000000000000",
                null,
                false,
                404
            }
        };
    }

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private void simpleGetRequestAndAssert(String endpoint, String operationKey, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)).andReturn();

        int status = result.getResponse().getStatus();
        Assert.assertEquals(status, expStatus);

        if (expSuccess) {
            String responseBody = result.getResponse().getContentAsString();
            String expResponseFile = RESPONSE_DIR + operationKey + "/" + fileKey + ".json";
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    @Test
    public void testGetReportSeries() throws Exception {
        simpleGetRequestAndAssert("/report-series", "index", "00", true, 200);
    }

    @Test(dataProvider = "getReportSeriesCases")
    public void testGetReportSeries(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert("/report-series/" + id, "show", fileKey, expSuccess, expStatus);
    }
}
