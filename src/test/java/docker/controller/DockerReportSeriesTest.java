package docker.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.ga4gh.testbed.api.testutils.ResourceLoader;

public class DockerReportSeriesTest {

    private static final String BASE_URL = "http://localhost:4500/report-series";

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
    public void testGetAllReportsSeries() throws Exception {
        simpleGetRequestAndAssert(BASE_URL, true, 200, "/responses/report-series/index/00.json");
    }

    @Test(dataProvider = "getReportSeriesCases")
    public void testGetReportSeries(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert(BASE_URL + "/" + id, expSuccess, expStatus, "/responses/report-series/show/" + fileKey + ".json");
    }
}
