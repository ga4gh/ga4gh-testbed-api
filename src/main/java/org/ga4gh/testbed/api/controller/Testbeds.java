package org.ga4gh.testbed.api.controller;

import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Testbed;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testbeds")
public class Testbeds {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private BasicShowRequestHandler<String, Testbed> showTestbed;

    @GetMapping
    @JsonView(SerializeView.TestbedSimple.class)
    public List<Testbed> getTestbeds() {
        return hibernateUtil.listEntityObject(Testbed.class);
    }

    @GetMapping(path = "/{testbedId:.+}")
    @JsonView(SerializeView.TestbedFull.class)
    public Testbed getTestbed(
        @PathVariable String testbedId
    ) {
        return showTestbed.prepare(testbedId).handleRequest();
    }


}
