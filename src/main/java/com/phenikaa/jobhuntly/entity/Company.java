package com.phenikaa.jobhuntly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Date;
import java.sql.Timestamp;
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

    private String logo;

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

    @ManyToMany
    @JoinTable(
            name = "company_industries",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "industry_id")
    )
    private Set<Industry> industries = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public void addIndustry(Industry industry) {
        industries.add(industry);
        industry.getCompanies().add(this);
    }
}
