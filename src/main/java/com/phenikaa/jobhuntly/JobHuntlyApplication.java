package com.phenikaa.jobhuntly;

import com.phenikaa.jobhuntly.entity.Category;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import com.phenikaa.jobhuntly.repository.CategoryRepository;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import com.phenikaa.jobhuntly.repository.JobRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class JobHuntlyApplication {

    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(JobHuntlyApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            if (userRepository.count() == 0 && categoryRepository.count() == 0 && jobRepository.count() == 0 && companyRepository.count() == 0) {
                User user = new User();
                user.setPassword(passwordEncoder.encode("password"));
                user.setUsername("phenikaa");
                user.setEmail("duynvm1711@gmail.com");
                user.setEnable(true);
                userRepository.save(user);

                Category category1 = new Category();
                category1.setName("Category1");
                categoryRepository.save(category1);

                Category category2 = new Category();
                category2.setName("Category2");
                categoryRepository.save(category2);

                Category category3 = new Category();
                category3.setName("Category3");
                categoryRepository.save(category3);

                Category category4 = new Category();
                category4.setName("Category4");
                categoryRepository.save(category4);

                Category category5 = new Category();
                category5.setName("Category5");
                categoryRepository.save(category5);

                Company company1 = new Company();
                company1.setName("Company1");
                company1.setDescription("Company 1");
                company1.setEmployees(5);
                companyRepository.save(company1);

                Job job1 = new Job();
                job1.setTitle("Job1");
                job1.setDescription("Job 1");
                job1.setType(JobType.FULL_TIME);
                job1.setJobLevel(JobLevel.FRESHER);
                job1.setCompany(company1);
                job1.setMinSalary(200);
                job1.setMaxSalary(5000);
                job1.addCategory(category1);
                job1.addCategory(category2);

                jobRepository.save(job1);
            }




        };
    }

}
