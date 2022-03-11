package org.ga4gh.testbed.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ga4gh.testbed.api.app.TestbedApi;
import org.ga4gh.testbed.api.app.TestbedApiSpringConfig;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.testutils.ResourceLoader;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
    Reports.class
})
@WebAppConfiguration
public class ReportsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    private MockMvc mockMvc;

    private static final String RESPONSE_DIR = "/responses/reports/";

    private static final String PAYLOAD_DIR = "/payloads/reports/";

    @DataProvider(name = "getReportCases")
    public Object[][] getReportCases() {
        return new Object[][] {
            {
                "00000000-0000-0000-0000-000000000000",
                null,
                false,
                404
            },
            {
                "01d0e947-5975-4786-a755-5025fec7416d",
                "00",
                true,
                200
            }
        };
    }

    @DataProvider(name = "createReportCases")
    public Object[][] createReportCases() {
        return new Object[][] {
            {
                "00000000-0000-0000-0000-000000000000",
                "abcdefg",
                "00",
                false,
                404
            },
            {
                "1edb5213-52a2-434f-a7b8-b101fea8fb30",
                "abcdefg",
                "00",
                false,
                401
            },
            {
                "483382e9-f92b-466d-9427-154d56a75fcf",
                "abcdefg",
                "00",
                false,
                401
            },
            {
                "1edb5213-52a2-434f-a7b8-b101fea8fb30",
                "K5pLbwScVu8rEoLLj8pRy5Wv7EXTVahn",
                "00",
                true,
                200
            },
            {
                "483382e9-f92b-466d-9427-154d56a75fcf",
                "l0HiRbbpjVDKc6k3tQ2skzROB1oAP2IV",
                "01",
                true,
                200
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
    public void testGetReports() throws Exception {
        simpleGetRequestAndAssert("/reports", "index", "00", true, 200);
    }

    @Test(dataProvider = "getReportCases")
    public void testGetReport(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert("/reports/" + id, "show", fileKey, expSuccess, expStatus);
    }

    @Test(dataProvider = "createReportCases", groups = "create")
    public void testCreateReport(String reportSeriesId, String reportSeriesToken, String fileKey, boolean expSuccess, int expStatus) throws Exception {

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("GA4GH-TestbedReportSeriesId", reportSeriesId);
        headers.add("GA4GH-TestbedReportSeriesToken", reportSeriesToken);

        // load request body
        String payloadFile = PAYLOAD_DIR + "create/" + fileKey + ".json";
        String requestBody = ResourceLoader.load(payloadFile);

        // perform request
        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/reports")
                .headers(headers)
                .content(requestBody)
        ).andReturn();

        // assert status code
        int status = result.getResponse().getStatus();
        Assert.assertEquals(status, expStatus);

        if (expSuccess) {
            // assert report json response after setting report id to "000..." (constant id)
            ObjectMapper mapper = new ObjectMapper();
            String responseBody = result.getResponse().getContentAsString();
            Report report = mapper.readValue(responseBody, Report.class);
            String deleteId = report.getId();
            report.setId("00000000-0000-0000-0000-000000000000");
            String finalResponseBody = mapper.writeValueAsString(report);
            
            // load exp response body
            String expResponseFile = RESPONSE_DIR + "create/" + fileKey + ".json";
            String expResponseBody = ResourceLoader.load(expResponseFile);

            Assert.assertEquals(finalResponseBody, expResponseBody);

            hibernateUtil.deleteEntityObject(Report.class, deleteId);
        }
    }
}
