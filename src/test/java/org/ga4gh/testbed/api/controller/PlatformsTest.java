package org.ga4gh.testbed.api.controller;

import org.ga4gh.testbed.api.app.TestbedApi;
import org.ga4gh.testbed.api.app.TestbedApiSpringConfig;
import org.ga4gh.testbed.api.model.Platform;
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
    Platform.class
})
@WebAppConfiguration
public class PlatformsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private static final String RESPONSE_DIR = "/responses/platforms/";

    @DataProvider(name = "getPlatformCases")
    public Object[][] getPlatformCases() {
        return new Object[][] {
            {
                "org.ga4gh.refget.starterkit",
                "00",
                true,
                200
            },
            {
                "org.ga4gh.rnaget.starterkit",
                "01",
                true,
                200
            },
            {
                "invalid.platform.id",
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
    public void testGetPlatforms() throws Exception {
        simpleGetRequestAndAssert("/platforms", "index", "00", true, 200);
    }

    @Test(dataProvider = "getPlatformCases")
    public void testGetPlatform(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert("/platforms/" + id, "show", fileKey, expSuccess, expStatus);
    }
}
