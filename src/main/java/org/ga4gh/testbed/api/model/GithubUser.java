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
@Table(name = "github_user")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUser implements HibernateEntity<String> {

    @Id
    @Column(name = "github_id", updatable = false, nullable = false)
    @JsonView(SerializeView.Always.class)
    private String githubId;

    @OneToMany(mappedBy = "githubUser",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonView(SerializeView.GithubUserSecure.class)
    private List<GithubUserOrganization> githubUserOrganizations;

    public void setId(String id) {
        this.githubId = id;
    }

    public String getId() {
        return githubId;
    }

    public void loadRelations() {
        Hibernate.initialize(getGithubUserOrganizations());
    }
}