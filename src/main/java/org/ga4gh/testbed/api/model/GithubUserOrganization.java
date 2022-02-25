package org.ga4gh.testbed.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.testbed.api.utils.SerializeView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_user_organization")
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUserOrganization implements HibernateEntity<Integer> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(SerializeView.Never.class)
    private Integer id;

    @Column(name = "role", nullable = false)
    @JsonView(SerializeView.Always.class)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "fk_github_user_github_id")
    @JsonView(SerializeView.OrganizationSecure.class)
    private GithubUser githubUser;

    @ManyToOne
    @JoinColumn(name = "fk_organization_id")
    @JsonView(SerializeView.GithubUserSecure.class)
    private Organization organization;

    public void loadRelations() {

    }
}
