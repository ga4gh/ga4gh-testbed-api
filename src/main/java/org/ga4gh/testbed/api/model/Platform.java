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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platform")
@Setter
@Getter
@NoArgsConstructor
public class Platform implements HibernateEntity<String> {

    @Id
    private String id;

    @Column
    private String platformName;

    @Column
    private String platformDescription;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_organization_id")
    private Organization organization;

    @ManyToMany
    @JoinTable(
        name = "specification_platform",
        joinColumns = {@JoinColumn(name = "fk_platform_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_specification_id")}
    )
    private List<Specification> specifications;

    @OneToMany(
        mappedBy = "platform",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ReportSeries> reportSeries;

    public void loadRelations() {
        Hibernate.initialize(getSpecifications());
        Hibernate.initialize(getReportSeries());
    }
}
