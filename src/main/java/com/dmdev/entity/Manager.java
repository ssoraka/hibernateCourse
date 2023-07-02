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
public class Manager extends User {

    private String projectName;

//    @Builder

    public Manager(Long id, String username, PersonalInfo personalInfo, LocalDate marriageDate, Birthday birthDate, Role role, String info, Company company, Profile profile, Set<UserChat> userChats, List<Payment> payments, String projectName) {
        super(id, username, personalInfo, marriageDate, birthDate, role, info, company, profile, userChats, payments);
        this.projectName = projectName;
    }
}
