package org.ga4gh.testbed.api.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.hibernate.Hibernate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Setter
@Getter
@NoArgsConstructor
public class Organization implements HibernateEntity<String> {

    @Id
    private String id;

    @Column
    private String organizationName;

    @Column
    private String organizationUrl;

    @ManyToMany
    @JoinTable(
        name = "user_organization",
        joinColumns = {@JoinColumn(name = "fk_organization_id")},
        inverseJoinColumns = {@JoinColumn(name = "fk_user_github_id")}
    )
    private List<GithubUser> githubUsers;

    @OneToMany(mappedBy = "organization",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Platform> platforms;

    public void loadRelations() {
        Hibernate.initialize(getGithubUsers());
        Hibernate.initialize(getPlatforms());
    }
}
