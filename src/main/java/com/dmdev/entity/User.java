package com.dmdev.entity;

import com.dmdev.converter.BirthdayConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonBlobType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



//@Inheritance(strategy = InheritanceType.JOINED)
//сложный селект с джоином всех таблиц

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//нужен общий sequence для двух таблиц
//в этом случае
//select by user - делает запрос в обе таблицы с union

//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type")
//не можем делать constraint
@NamedQuery(name = "findUserByName", query = "select u from User u " +
        "join u.company c " +
        "where u.personalInfo.firstname = :firstname and c.name = :companyName " +
        "order by u.personalInfo.lastname desc ")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder
@Entity
@Table(name = "users" /*, schema = "public"*/)
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Id
//    @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
//    private Long id;

    @Column(unique = true)
    private String username;

    @Embedded
    @AttributeOverride(name = "firstname", column = @Column(name = "firstname")) //переопределение названия столбца
    private PersonalInfo personalInfo;

    @Column(name = "marriage_date")
    private LocalDate marriageDate;

    @Convert(converter = BirthdayConverter.class)
    @Column(name = "birth_date")
    private Birthday birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

//    не поддерживается H2
//    в тестконтейнере тоже не запустилось
//    @Type(value = JsonBinaryType.class)
    private String info;

//    optional = true - left join
//    optional = false - join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;

//    нет ленивой инициализации, тк в любом случае надо делать запрос в бд, чтоб выявить наличие связи
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Profile profile;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
//    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();


//    @ManyToMany
//    @JoinTable(name = "users_chat",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "chat_id"))
//    private Set<Chat> chats = new HashSet<>();
//
//    public void addChat(Chat chat) {
//        chats.add(chat);
//        chat.getUsers().add(this);
//    }
}
