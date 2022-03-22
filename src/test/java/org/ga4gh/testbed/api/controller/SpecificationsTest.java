package org.ga4gh.testbed.api.controller;

import org.ga4gh.testbed.api.app.TestbedApi;
import org.ga4gh.testbed.api.app.TestbedApiSpringConfig;
import org.ga4gh.testbed.api.testutils.ResourceLoader;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
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
    Specifications.class
})
@WebAppConfiguration
public class SpecificationsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    private MockMvc mockMvc;

    private static final String RESPONSE_DIR = "/responses/specifications/";

    @DataProvider(name = "getSpecificationCases")
    public Object[][] getSpecificationCases() {
        return new Object[][] {
            {
                "refget",
                "00",
                true,
                200
            },
            {
                "rnaget",
                "01",
                true,
                200
            },
            {
                "invalidid",
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
    public void testGetSpecifications() throws Exception {
        simpleGetRequestAndAssert("/specifications", "index", "00", true, 200);
    }

    @Test(dataProvider = "getSpecificationCases")
    public void testGetSpecification(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert("/specifications/" + id, "show", fileKey, expSuccess, expStatus);
    }

}
