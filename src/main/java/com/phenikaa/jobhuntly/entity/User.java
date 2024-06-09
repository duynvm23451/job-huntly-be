package com.phenikaa.jobhuntly.entity;

import com.phenikaa.jobhuntly.enums.Gender;
import com.phenikaa.jobhuntly.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @NotNull(message = "Email không được để trống")
    private String email;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @Column(name = "cover_file_name")
    private String coverFileName;

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Column(name = "full_name")
    private String fullName;

    private Role role;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Gender gender;

    @Column(name = "is_enable")
    private boolean isEnable;

    @Column(name = "type_notification_accept", columnDefinition = "TEXT")
    private String typeNotificationAccept;

    private String address;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
