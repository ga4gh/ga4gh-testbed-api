package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OrganizationTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "org.ga4gh",
                "Global Alliance for Genomics and Health",
                "https://ga4gh.org",
                new ArrayList<GithubUser>() {{
                    add(new GithubUser() {{
                        setGithubId("jb-adams");
                    }});
                }},
                new ArrayList<Platform>() {{
                    add(new Platform() {{
                        setId("org.ga4gh.refget.starterkit");
                    }});
                }}
            }
        };
    }

    @Test(dataProvider = "cases")
    public void testOrganization(String id, String organizationName, String organizationUrl,
        List<GithubUser> githubUsers, List<Platform> platforms) {

        Organization organization = new Organization();
        organization.loadRelations();

        organization.setId(id);
        organization.setOrganizationName(organizationName);
        organization.setOrganizationUrl(organizationUrl);
        organization.setGithubUsers(githubUsers);
        organization.setPlatforms(platforms);

        Assert.assertEquals(organization.getId(), id);
        Assert.assertEquals(organization.getOrganizationName(), organizationName);
        Assert.assertEquals(organization.getOrganizationUrl(), organizationUrl);
        Assert.assertEquals(organization.getGithubUsers(), githubUsers);
        Assert.assertEquals(organization.getPlatforms(), platforms);
    }
}
