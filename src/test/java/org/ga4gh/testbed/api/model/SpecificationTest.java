package org.ga4gh.testbed.api.model;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SpecificationTest {

    @DataProvider(name = "cases")
    public Object[][] getData() {
        return new Object[][] {
            {
                "refget",
                "refget",
                "Reference sequence retrieval API",
                "https://github.com/samtools/hts-specs",
                "https://samtools.github.io/hts-specs/refget.html",
                new ArrayList<Testbed>() {{
                    add(new Testbed() {{
                        setId("refget-compliance-suite");
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
    public void testSpecification(String id, String specName, String specDescription,
        String githubUrl, String documentationUrl, List<Testbed> testbeds,
        List<Platform> platforms) {

        Specification specification = new Specification();
        specification.setId(id);
        specification.setSpecName(specName);
        specification.setSpecDescription(specDescription);
        specification.setGithubUrl(githubUrl);
        specification.setDocumentationUrl(documentationUrl);
        specification.setTestbeds(testbeds);
        specification.setPlatforms(platforms);

        Assert.assertEquals(specification.getId(), id);
        Assert.assertEquals(specification.getSpecName(), specName);
        Assert.assertEquals(specification.getSpecDescription(), specDescription);
        Assert.assertEquals(specification.getGithubUrl(), githubUrl);
        Assert.assertEquals(specification.getDocumentationUrl(), documentationUrl);
        Assert.assertEquals(specification.getTestbeds(), testbeds);
        Assert.assertEquals(specification.getPlatforms(), platforms);
    }
}
