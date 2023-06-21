package com.dmdev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User {

    private String projectName;

    @Builder
    public Manager(Long id, String username, PersonalInfo personalInfo, LocalDate marriageDate, Birthday birthDate, Role role, String info, Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, username, personalInfo, marriageDate, birthDate, role, info, company, profile, userChats);
        this.projectName = projectName;
    }
}
