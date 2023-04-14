package com.company.entity;

import com.company.enums.ProfileRoleEnum;
import com.company.enums.ProfileStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column
    private String password;
    @Column
    private boolean visible;
    @Column
    @Enumerated(value = EnumType.STRING)
    private ProfileStatusEnum status;
    @Column
    @Enumerated(value = EnumType.STRING)
    private ProfileRoleEnum role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
