package org.ga4gh.testbed.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.testbed.api.utils.SerializeView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "summary")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Summary implements HibernateEntity<Long> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(SerializeView.Never.class)
    private Long id;

    @Column(name = "unknown", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Integer unknown;

    @Column(name = "passed", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Integer passed;

    @Column(name = "warned", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Integer warned;

    @Column(name = "failed", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Integer failed;

    @Column(name = "skipped", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Integer skipped;

    @OneToOne(mappedBy = "summary",
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    private Report report;

    @OneToOne(mappedBy = "summary",
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    private Phase phase;

    @OneToOne(mappedBy = "summary",
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    private TestbedTest testbedTest;

    public void loadRelations() {
        
    }
}
