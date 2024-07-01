package com.phenikaa.jobhuntly.entity;

import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
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
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Tiêu đề không được trống")
    private String title;

    @NotNull(message = "Mô tả không được trống")
    private String description;

    private JobType type;

    @Column(name = "min_salary")
    private int minSalary;

    @Column(name = "max_salary")
    private int maxSalary;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "nice_to_haves", columnDefinition = "TEXT")
    private String niceToHaves;

    @Column(name = "preferred_qualifications", columnDefinition = "TEXT")
    private String preferredQualifications;

    @Column(name = "perk_and_benefits", columnDefinition = "Text")
    private String perkAndBenefits;

    @Column(name = "number_of_recruits")
    private int numberOfRecruits;

    @Column(name = "job_level")
    private JobLevel jobLevel;

    @Temporal(TemporalType.DATE)
    private Date deadline;


    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> users;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobCategory> categories = new HashSet<>();


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public void addCategory(Category category) {
        JobCategory jobCategory = new JobCategory(this, category);
        categories.add(jobCategory);
        category.getJobs().add(jobCategory);
    }

    public void removeCategory(Category category) {
        JobCategory jobCategory = new JobCategory(this, category);
        categories.remove(jobCategory);
        category.getJobs().remove(jobCategory);
    }
}
