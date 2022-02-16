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
@Table(name = "report_series")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportSeries implements HibernateEntity<UUID> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue
    @JsonView(SerializeView.Always.class)
    private UUID id;

    @Column(name = "token_salt")
    @JsonView(SerializeView.Never.class)
    private String tokenSalt;

    @Column(name = "token_hash")
    @JsonView(SerializeView.Never.class)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_testbed_id")
    @JsonView(SerializeView.Never.class)
    private Testbed testbed;

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                          CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "fk_platform_id")
    @JsonView(SerializeView.Never.class)
    private Platform platform;

    @OneToMany(
        mappedBy = "reportSeries",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonView(SerializeView.Never.class)
    private List<Report> reports;

    public void loadRelations() {
        Hibernate.initialize(getReports());
    }
}
