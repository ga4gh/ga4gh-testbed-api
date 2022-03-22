package org.ga4gh.testbed.api.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Platform;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platforms")
public class Platforms {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private BasicShowRequestHandler<String, Platform> showPlatform;

    @GetMapping
    @JsonView(SerializeView.PlatformSimple.class)
    public List<Platform> getPlatforms() {
        return hibernateUtil.listEntityObject(Platform.class);
    }

    @GetMapping(path = "/{platformId:.+}")
    @JsonView(SerializeView.PlatformFull.class)
    public Platform getPlatform(
        @PathVariable String platformId
    ) {
        return showPlatform.prepare(platformId).handleRequest();
    }
}
