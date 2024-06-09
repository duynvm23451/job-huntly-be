package com.phenikaa.jobhuntly.entity;

import com.phenikaa.jobhuntly.enums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    @Column(name = "number_of_recruits")
    private int numberOfRecruits;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name="job_level_id")
    private JobLevel jobLevel;


    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private java.util.Date created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
