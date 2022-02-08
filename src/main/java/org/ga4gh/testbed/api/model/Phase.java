package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phase")
@Setter
@Getter
@NoArgsConstructor
public class Phase implements HibernateEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String phaseName;

    @Column
    private String phaseDescription;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
    private LocalDateTime startTime;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
    private LocalDateTime endTime;

    @Column
    private Status status;

    @OneToOne(cascade = CascadeType.ALL,
              orphanRemoval = true)
    @JoinColumn(name = "fk_summary_id", referencedColumnName = "id")
    private Summary summary;

    @OneToMany(mappedBy = "phase",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<TestbedTest> testbedTests;

    @ManyToOne
    @JoinColumn(name = "fk_report_id")
    private Report report;

    public void loadRelations() {
        Hibernate.initialize(getTestbedTests());
    }
}
