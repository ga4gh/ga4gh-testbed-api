package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Setter
@Getter
@NoArgsConstructor
public class Report implements HibernateEntity<String> {

    private String id;

    private Map<String, String> inputParameters;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Status status;

    private Summary summary;

    private List<Phase> phases;

    public void loadRelations() {
        Hibernate.initialize(getPhases());
    }
}
