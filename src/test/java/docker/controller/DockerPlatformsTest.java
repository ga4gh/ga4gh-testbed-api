package docker.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.ga4gh.testbed.api.testutils.ResourceLoader;

public class DockerPlatformsTest {

    private static final String BASE_URL = "http://localhost:4500/platforms";

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
    public void testGetPlatforms() throws Exception {
        simpleGetRequestAndAssert(BASE_URL, true, 200, "/responses/platforms/index/00.json");
    }

    @Test(dataProvider = "getPlatformCases")
    public void testGetPlatforms(String id, String fileKey, boolean expSuccess, int expStatus) throws Exception {
        simpleGetRequestAndAssert(BASE_URL + "/" + id, expSuccess, expStatus, "/responses/platforms/show/" + fileKey + ".json");
    }
}
