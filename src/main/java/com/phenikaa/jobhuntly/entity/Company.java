package com.phenikaa.jobhuntly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String location;

    private int employees;

    @Column(name = "date_founded")
    private Date dateFounded;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @Column(name = "linkedin_link")
    private String linkedinLink;

    @Column(name = "website_link")
    private String websiteLink;

    @OneToMany(mappedBy="company")
    private Set<User> users;

    @OneToMany(mappedBy="company")
    private Set<Job> jobs;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private java.util.Date created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
