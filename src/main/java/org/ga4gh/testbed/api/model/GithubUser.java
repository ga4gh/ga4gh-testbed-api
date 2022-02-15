package org.ga4gh.testbed.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_user")
@Setter
@Getter
@NoArgsConstructor
public class GithubUser implements HibernateEntity<String> {

    @Id
    private String githubId;

    @ManyToMany
    @JoinTable(
        name = "user_organization",
        joinColumns = {@JoinColumn(name = "fk_user_github_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_organization_id")}
    )
    private List<Organization> organizations;

    public void setId(String id) {
        this.githubId = id;
    }

    public String getId() {
        return githubId;
    }

    public void loadRelations() {
        Hibernate.initialize(getOrganizations());
    }
}