package com.phenikaa.jobhuntly.entity;

import com.phenikaa.jobhuntly.enums.Gender;
import com.phenikaa.jobhuntly.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> jobs;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoom> chatRoomsAsUser1;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoom> chatRoomsAsUser2;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
