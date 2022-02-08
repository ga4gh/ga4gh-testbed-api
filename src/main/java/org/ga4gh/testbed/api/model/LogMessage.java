package org.ga4gh.testbed.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_message")
@Setter
@Getter
@NoArgsConstructor
public class LogMessage implements HibernateEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String message;

    @ManyToOne
    @JoinColumn(name = "fk_testbed_case_id")
    private TestbedCase testbedCase;

    public void loadRelations() {

    }
}
