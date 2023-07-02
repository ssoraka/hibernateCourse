package com.dmdev.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@PrimaryKeyJoinColumn(name = "id")
public class Programmer extends User {

    @Enumerated(EnumType.STRING)
    private Language language;

//    @Builder

    public Programmer(Long id, String username, PersonalInfo personalInfo, LocalDate marriageDate, Birthday birthDate, Role role, String info, Company company, Profile profile, Set<UserChat> userChats, List<Payment> payments, Language language) {
        super(id, username, personalInfo, marriageDate, birthDate, role, info, company, profile, userChats, payments);
        this.language = language;
    }
}
