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
                new ArrayList<GithubUserOrganization>() {{
                    add(new GithubUserOrganization() {{
                        setId(Integer.valueOf(0));
                    }});
                }},
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testGithubUser(String githubId, List<GithubUserOrganization> githubUserOrganizations) {

        GithubUser githubUser = new GithubUser();
        githubUser.loadRelations();
        
        githubUser.setGithubId(githubId);
        githubUser.setGithubUserOrganizations(githubUserOrganizations);
        Assert.assertEquals(githubUser.getGithubId(), githubId);
        Assert.assertEquals(githubUser.getGithubUserOrganizations(), githubUserOrganizations);

        githubUser.setId(githubId);
        Assert.assertEquals(githubUser.getId(), githubId);
    }
}
