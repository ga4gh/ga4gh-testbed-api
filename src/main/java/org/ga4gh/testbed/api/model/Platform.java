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
@Table(name = "platform")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Platform implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(SerializeView.Always.class)
    private String id;

    @Column(name = "platform_name")
    @JsonView(SerializeView.Always.class)
    private String platformName;

    @Column(name = "platform_description")
    @JsonView(SerializeView.Always.class)
    private String platformDescription;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_organization_id")
    @JsonView(SerializeView.PlatformFull.class)
    private Organization organization;

    @ManyToMany
    @JoinTable(
        name = "specification_platform",
        joinColumns = {@JoinColumn(name = "fk_platform_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_specification_id")}
    )
    @JsonView(SerializeView.PlatformFull.class)
    private List<Specification> specifications;

    @OneToMany(
        mappedBy = "platform",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonView(SerializeView.PlatformFull.class)
    private List<ReportSeries> reportSeries;

    public void loadRelations() {
        Hibernate.initialize(getSpecifications());
        Hibernate.initialize(getReportSeries());
    }
}
