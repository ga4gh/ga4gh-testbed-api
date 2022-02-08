package org.ga4gh.testbed.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "summary")
@Setter
@Getter
@NoArgsConstructor
public class Summary implements HibernateEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Integer unknown;

    @Column
    private Integer passed;

    @Column
    private Integer warned;

    @Column
    private Integer failed;

    @Column
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
    private TestbedTest test;

    @OneToOne(mappedBy = "summary",
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    private TestbedCase testCase;

    public void loadRelations() {
        
    }
}
