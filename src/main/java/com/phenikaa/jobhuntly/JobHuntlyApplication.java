package com.phenikaa.jobhuntly;

import com.phenikaa.jobhuntly.entity.*;
import com.phenikaa.jobhuntly.enums.JobLevel;
import com.phenikaa.jobhuntly.enums.JobType;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class JobHuntlyApplication {

    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final IndustryRepository industryRepository;
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
                user.setRole(Role.RECRUITER);
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

                Industry industry1 = new Industry();
                industry1.setName("Industry1");
                industryRepository.save(industry1);

                Industry industry2 = new Industry();
                industry2.setName("Industry2");
                industryRepository.save(industry2);

                Company company1 = new Company();
                company1.setName("Company1");
                company1.setDescription("Company 1");
                company1.setEmployees(5);
                company1.addIndustry(industry1);
                company1.addIndustry(industry2);
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
