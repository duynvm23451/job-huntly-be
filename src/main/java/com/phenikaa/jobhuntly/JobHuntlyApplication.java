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

                Category category1 = new Category();
                category1.setName("Thiết kế");
                categoryRepository.save(category1);

                Category category2 = new Category();
                category2.setName("Tiếp thị");
                categoryRepository.save(category2);

                Category category3 = new Category();
                category3.setName("Marketing");
                categoryRepository.save(category3);

                Category category4 = new Category();
                category4.setName("Tài chính");
                categoryRepository.save(category4);

                Category category5 = new Category();
                category5.setName("Công nghệ");
                categoryRepository.save(category5);

                Category category6 = new Category();
                category6.setName("Lập trình viên");
                categoryRepository.save(category6);

                Category category7 = new Category();
                category7.setName("Kinh doanh");
                categoryRepository.save(category7);

                Category category8 = new Category();
                category8.setName("Tuyển dụng");
                categoryRepository.save(category8);

                Industry industry1 = new Industry();
                industry1.setName("Bất động sản");
                industryRepository.save(industry1);

                Industry industry2 = new Industry();
                industry2.setName("Dịch vụ");
                industryRepository.save(industry2);

                Industry industry3 = new Industry();
                industry3.setName("Sản xuất");
                industryRepository.save(industry3);

                Industry industry4 = new Industry();
                industry4.setName("Truyền thông");
                industryRepository.save(industry4);

                Industry industry5 = new Industry();
                industry5.setName("Công nghệ");
                industryRepository.save(industry5);

            }




        };
    }

}
