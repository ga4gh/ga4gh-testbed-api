package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GithubUserTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "jb-adams",
                new ArrayList<Organization>() {{
                    add(new Organization() {{
                        setId("org.ga4gh");
                    }});
                }},
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testGithubUser(String githubId, List<Organization> organizations) {

        GithubUser githubUser = new GithubUser();
        githubUser.loadRelations();
        
        githubUser.setGithubId(githubId);
        githubUser.setOrganizations(organizations);
        Assert.assertEquals(githubUser.getGithubId(), githubId);
        Assert.assertEquals(githubUser.getOrganizations(), organizations);

        githubUser.setId(githubId);
        Assert.assertEquals(githubUser.getId(), githubId);
    }
}
