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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testbed_test")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TestbedTest implements HibernateEntity<Integer> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(SerializeView.Never.class)
    private Integer id;

    @Column(name = "test_name", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String testName;

    @Column(name = "test_description", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String testDescription;

    @Column(name = "start_time", nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
    @JsonView(SerializeView.Always.class)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_FORMAT)
    @JsonView(SerializeView.Always.class)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL,
              orphanRemoval = true)
    @JoinColumn(name = "fk_summary_id", referencedColumnName = "id")
    @JsonView(SerializeView.ReportFull.class)
    private Summary summary;

    @OneToMany(mappedBy = "testbedTest",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
               @JsonView(SerializeView.ReportFull.class)
    private List<TestbedCase> cases;

    @ManyToOne
    @JoinColumn(name = "fk_phase_id")
    @JsonView(SerializeView.Never.class)
    private Phase phase;

    public void loadRelations() {
        Hibernate.initialize(getCases());
    }
}
