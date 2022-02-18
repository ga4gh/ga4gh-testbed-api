package org.ga4gh.testbed.api.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import org.hibernate.annotations.Cascade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testbed_case")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TestbedCase implements HibernateEntity<Long> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(SerializeView.Never.class)
    private Long id;

    @Column(name = "case_name", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String caseName;

    @Column(name = "case_description")
    @JsonView(SerializeView.Always.class)
    private String caseDescription;

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

    @Column(name = "message", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String message;

    // @OneToMany(mappedBy = "testbedCase",
               // fetch = FetchType.LAZY,
               // cascade = CascadeType.ALL,
               // orphanRemoval = true)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "log_message", joinColumns = @JoinColumn(name = "fk_testbed_case_id"))
    @Column(name = "message")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
    @JsonView(SerializeView.ReportFull.class)
    // @JsonManagedReference
    private List<String> logMessages;
    // private List<LogMessage> logMessages;

    @ManyToOne
    @JoinColumn(name = "fk_testbed_test_id")
    @JsonBackReference
    @JsonView(SerializeView.Never.class)
    private TestbedTest testbedTest;

    public void loadRelations() {
        Hibernate.initialize(getLogMessages());
    }
}
