package docker.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.ga4gh.testbed.api.model.Report;
import org.ga4gh.testbed.api.testutils.ResourceLoader;

public class DockerReportsTest {

    private static final String BASE_URL = "http://localhost:4500/reports";

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

    private void simpleGetRequestAndAssert(String url, boolean expSuccess, int expStatus, String expResponseFile) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertEquals(response.statusCode(), expStatus);
        if (expSuccess) {
            String responseBody = response.body();
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(responseBody, expResponseBody);
        }
    }

    @Test
    public void testGetReports() throws Exception {
        simpleGetRequestAndAssert(BASE_URL, true, 200, "/responses/reports/index/00.json");
    }

    @Test(dataProvider = "getReportCases")
    public void testGetReport(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert(BASE_URL + "/" + id, expSuccess, expStatus, "/responses/reports/show/" + fileKey + ".json");
    }

    @Test(dataProvider = "createReportCases", groups = "create")
    public void testCreateReport(String reportSeriesId, String reportSeriesToken, String fileKey, boolean expSuccess, int expStatus) throws Exception {

        String payloadFile = "/responses/reports/create/" + fileKey + ".json";
        String requestBody = ResourceLoader.load(payloadFile);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Content-Type", "application/json")
            .header("GA4GH-TestbedReportSeriesId", reportSeriesId)
            .header("GA4GH-TestbedReportSeriesToken", reportSeriesToken)
            .POST(BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("***");
        System.out.println(response.body());
        System.out.println(response.statusCode());
        System.out.println("***");

        Assert.assertEquals(response.statusCode(), expStatus);
        if (expSuccess) {
            ObjectMapper mapper = new ObjectMapper();
            String responseBody = response.body();
            Report report = mapper.readValue(responseBody, Report.class);
            report.setId("00000000-0000-0000-0000-000000000000");
            String finalResponseBody = mapper.writeValueAsString(report);

            // load exp response body
            String expResponseFile = "/responses/reports/create/" + fileKey + ".json";
            String expResponseBody = ResourceLoader.load(expResponseFile);
            Assert.assertEquals(finalResponseBody, expResponseBody);
        }
    }
}
