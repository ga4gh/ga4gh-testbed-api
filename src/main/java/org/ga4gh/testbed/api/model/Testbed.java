package org.ga4gh.testbed.api.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "testbed")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Testbed implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(SerializeView.Always.class)
    private String id;

    @Column(name = "testbed_name")
    @JsonView(SerializeView.Always.class)
    private String testbedName;

    @Column(name = "testbed_description")
    @JsonView(SerializeView.Always.class)
    private String testbedDescription;

    @Column(name = "repo_url")
    @JsonView(SerializeView.Always.class)
    private String repoUrl;

    @Column(name = "dockerhub_url")
    @JsonView(SerializeView.Always.class)
    private String dockerhubUrl;

    @Column(name = "dockstore_url")
    @JsonView(SerializeView.Always.class)
    private String dockstoreUrl;

    @ManyToMany
    @JoinTable(
        name = "specification_testbed",
        joinColumns = {@JoinColumn(name = "fk_testbed_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_specification_id")}
    )
    @JsonView(SerializeView.TestbedFull.class)
    private List<Specification> specifications;

    @OneToMany(
        mappedBy = "testbed",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonView(SerializeView.Never.class)
    private List<ReportSeries> reportSeries;

    @OneToMany(
        mappedBy = "testbed",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonView(SerializeView.Never.class)
    private List<TestbedVersion> testbedVersions;

    public void loadRelations() {
        Hibernate.initialize(getSpecifications());
        // Hibernate.initialize(getReportSeries());
        // Hibernate.initialize(getTestbedVersions());
    }
}
