package org.ga4gh.testbed.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "specification")
@Setter
@Getter
@NoArgsConstructor
public class Specification implements HibernateEntity<String> {

    @Id
    private String id;

    @Column
    private String specName;

    @Column
    private String specDescription;

    @Column
    private String githubUrl;

    @Column
    private String documentationUrl;

    public void loadRelations() {
        
    }
}
