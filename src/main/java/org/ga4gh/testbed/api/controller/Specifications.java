package org.ga4gh.testbed.api.controller;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonView;
import org.ga4gh.starterkit.common.requesthandler.BasicShowRequestHandler;
import org.ga4gh.testbed.api.model.Specification;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.ga4gh.testbed.api.utils.hibernate.TestbedApiHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specifications")
public class Specifications {

    @Autowired
    private TestbedApiHibernateUtil hibernateUtil;

    @Autowired
    private BasicShowRequestHandler<String, Specification> showSpecification;

    @GetMapping
    @JsonView(SerializeView.SpecificationSimple.class)
    public List<Specification> getSpecifications() {
        return hibernateUtil.listEntityObject(Specification.class);
    }

    @GetMapping(path = "/{specificationId:.+}")
    @JsonView(SerializeView.SpecificationFull.class)
    public Specification getSpecification(
        @PathVariable String specificationId
    ) {
        return showSpecification.prepare(specificationId).handleRequest();
    }
}
