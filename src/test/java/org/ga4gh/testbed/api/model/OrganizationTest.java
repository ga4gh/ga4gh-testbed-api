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
                new ArrayList<GithubUserOrganization>() {{
                    add(new GithubUserOrganization() {{
                        setId(Integer.valueOf(0));
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
        List<GithubUserOrganization> githubUserOrganizations, List<Platform> platforms) {

        Organization organization = new Organization();
        organization.loadRelations();

        organization.setId(id);
        organization.setOrganizationName(organizationName);
        organization.setOrganizationUrl(organizationUrl);
        organization.setGithubUserOrganizations(githubUserOrganizations);
        organization.setPlatforms(platforms);

        Assert.assertEquals(organization.getId(), id);
        Assert.assertEquals(organization.getOrganizationName(), organizationName);
        Assert.assertEquals(organization.getOrganizationUrl(), organizationUrl);
        Assert.assertEquals(organization.getGithubUserOrganizations(), githubUserOrganizations);
        Assert.assertEquals(organization.getPlatforms(), platforms);
    }
}
