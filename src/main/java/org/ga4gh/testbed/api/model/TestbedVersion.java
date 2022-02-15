package org.ga4gh.testbed.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testbed_version")
@Setter
@Getter
@NoArgsConstructor
public class TestbedVersion implements HibernateEntity<Integer> {

    @Id
    private Integer id;

    @Column
    private String testbedVersion;

    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                   CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(name = "fk_testbed_id")
    private Testbed testbed;

    public void loadRelations() {

    }
}
