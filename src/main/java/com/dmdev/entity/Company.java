package com.dmdev.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Builder
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @Builder.Default
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//    @JoinColumn(name = "company_id") //можно указывать без связи @ManyToOne
//    sql OrderBy
//    @jakarta.persistence.OrderBy("username DESC, personalInfo.lastname ASC") // hql
//    @org.hibernate.annotations.OrderBy(clause = "username DESC, lastname ASC") // sql

//    in memory TreeSet TreeMap
//    @SortNatural //подставляет SortedSet, но User должен быть Comparable
//    @SortComparator()
//    private Set<User> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @MapKey(name = "username")
    private Map<String, User> users = new HashMap<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
//    @AttributeOverride(name = "lang", column = @Column(name = "lang"))
    private List<LocaleInfo> locales = new ArrayList<>();
//    для справочных таблиц
//    @Column(name = "description")
//    private List<String> locales = new ArrayList<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}
