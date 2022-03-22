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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
@ContextConfiguration(classes = {
    TestbedApi.class,
    TestbedApiSpringConfig.class,
    TestController.class
})
@WebAppConfiguration
public class TestControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private static final String JSON_DIR = "/responses/test-controller/";

    @BeforeMethod
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private void requestAndAssert(String endpoint, String expJsonFile) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expResponseBody = ResourceLoader.load(expJsonFile);
        Assert.assertEquals(responseBody, expResponseBody);
    }

    @Test
    public void testGetExampleSpecification() throws Exception {
        requestAndAssert("/test/specification", JSON_DIR + "specification.json");
    }

    @Test
    public void testGetExampleOrganization() throws Exception {
        requestAndAssert("/test/organization", JSON_DIR + "organization.json");
    }

    @Test
    public void testGetExampleReportSeries() throws Exception {
        requestAndAssert("/test/report-series", JSON_DIR + "report-series.json");
    }

    @Test
    public void testGetExampleReport() throws Exception {
        requestAndAssert("/test/report", JSON_DIR + "report.json");
    }
}
