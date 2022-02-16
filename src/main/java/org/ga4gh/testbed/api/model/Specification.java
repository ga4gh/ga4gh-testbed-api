package org.ga4gh.testbed.api.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "specification")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Specification implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(SerializeView.Always.class)
    private String id;

    @Column(name = "spec_name")
    @JsonView(SerializeView.Always.class)
    private String specName;

    @Column(name = "spec_description")
    @JsonView(SerializeView.Always.class)
    private String specDescription;

    @Column(name = "github_url")
    @JsonView(SerializeView.Always.class)
    private String githubUrl;

    @Column(name = "documentation_url")
    @JsonView(SerializeView.Always.class)
    private String documentationUrl;

    @ManyToMany
    @JoinTable(
        name = "specification_testbed",
        joinColumns = {@JoinColumn(name = "fk_specification_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_testbed_id")}
    )
    @JsonView(SerializeView.SpecificationFull.class)
    private List<Testbed> testbeds;

    @ManyToMany
    @JoinTable(
        name = "specification_platform",
        joinColumns = {@JoinColumn(name = "fk_specification_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_platform_id")}
    )
    @JsonView(SerializeView.SpecificationFull.class)
    private List<Platform> platforms;

    public void loadRelations() {
        Hibernate.initialize(getTestbeds());
        Hibernate.initialize(getPlatforms());
    }
}
