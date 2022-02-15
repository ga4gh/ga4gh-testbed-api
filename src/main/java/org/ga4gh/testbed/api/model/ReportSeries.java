package org.ga4gh.testbed.api.model;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_series")
@Setter
@Getter
@NoArgsConstructor
public class ReportSeries implements HibernateEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String tokenSalt;

    @Column
    private String tokenHash;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_testbed_id")
    private Testbed testbed;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_platform_id")
    private Platform platform;

    @OneToMany(
        mappedBy = "reportSeries",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Report> reports;

    public void loadRelations() {
        Hibernate.initialize(getReports());
    }
}
