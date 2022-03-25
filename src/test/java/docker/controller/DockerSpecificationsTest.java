package docker.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.ga4gh.testbed.api.testutils.ResourceLoader;

public class DockerSpecificationsTest {

    private static final String BASE_URL = "http://localhost:4500/specifications";

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
    public void testGetSpecifications() throws Exception {
        simpleGetRequestAndAssert(BASE_URL, true, 200, "/responses/specifications/index/00.json");
    }

    @Test(dataProvider = "getSpecificationCases")
    public void testGetSpecification(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert(BASE_URL + "/" + id, expSuccess, expStatus, "/responses/specifications/show/" + fileKey + ".json");
    }
}
