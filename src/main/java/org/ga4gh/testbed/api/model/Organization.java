package org.ga4gh.testbed.api.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.testbed.api.utils.SerializeView;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Organization implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JsonView(SerializeView.Always.class)
    private String id;

    @Column(name = "organization_name", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String organizationName;

    @Column(name = "organization_url", nullable = false)
    @JsonView(SerializeView.Always.class)
    private String organizationUrl;

    @OneToMany(mappedBy = "organization",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.OrganizationSecure.class)
    private List<GithubUserOrganization> githubUserOrganizations;

    @OneToMany(mappedBy = "organization",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.OrganizationFull.class)
    private List<Platform> platforms;

    public void loadRelations() {
        Hibernate.initialize(getGithubUserOrganizations());
        Hibernate.initialize(getPlatforms());
    }
}
