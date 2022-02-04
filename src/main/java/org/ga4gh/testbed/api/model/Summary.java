package org.ga4gh.testbed.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "summary")
@Setter
@Getter
@NoArgsConstructor
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
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

    @OneToOne(mappedBy = "summary")
    private Report report;

    @OneToOne(mappedBy = "summary")
    private Phase phase;

    @OneToOne(mappedBy = "summary")
    private Test test;

    @OneToOne(mappedBy = "summary")
    private TestCase testCase;
}
