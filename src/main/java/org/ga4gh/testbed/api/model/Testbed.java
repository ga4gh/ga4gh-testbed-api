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
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testbed")
@Setter
@Getter
@NoArgsConstructor
public class Testbed implements HibernateEntity<String> {

    @Id
    private String id;

    @Column
    private String testbedName;

    @Column
    private String testbedDescription;

    @Column
    private String repoUrl;

    @Column
    private String dockerhubUrl;

    @Column
    private String dockstoreUrl;

    @ManyToMany
    @JoinTable(
        name = "specification_testbed",
        joinColumns = {@JoinColumn(name = "fk_testbed_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_specification_id")}
    )
    private List<Specification> specifications;

    @OneToMany(
        mappedBy = "testbed",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ReportSeries> reportSeries;

    @OneToMany(
        mappedBy = "testbed",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TestbedVersion> testbedVersions;

    public void loadRelations() {
        Hibernate.initialize(getSpecifications());
        Hibernate.initialize(getReportSeries());
        Hibernate.initialize(getTestbedVersions());
    }
}
