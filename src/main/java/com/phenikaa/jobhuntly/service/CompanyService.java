package com.phenikaa.jobhuntly.service;

import com.phenikaa.jobhuntly.dto.CompanyDTO;
import com.phenikaa.jobhuntly.dto.JobDTO;
import com.phenikaa.jobhuntly.dto.UserDTO;
import com.phenikaa.jobhuntly.entity.Company;
import com.phenikaa.jobhuntly.entity.Industry;
import com.phenikaa.jobhuntly.entity.Job;
import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.Role;
import com.phenikaa.jobhuntly.exception.ObjectNotFoundException;
import com.phenikaa.jobhuntly.exception.SharedException;
import com.phenikaa.jobhuntly.mapper.CompanyMapper;
import com.phenikaa.jobhuntly.mapper.JobMapper;
import com.phenikaa.jobhuntly.mapper.UserMapper;
import com.phenikaa.jobhuntly.repository.CompanyRepository;
import com.phenikaa.jobhuntly.repository.IndustryRepository;
import com.phenikaa.jobhuntly.repository.JobRepository;
import com.phenikaa.jobhuntly.repository.UserRepository;
import com.phenikaa.jobhuntly.s3.S3ImageUploader;
import com.phenikaa.jobhuntly.specification.CompanySpecification;
import com.phenikaa.jobhuntly.specification.JobSpecification;
import com.phenikaa.jobhuntly.specification.filter.CompanyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final S3ImageUploader s3ImageUploader;
    private final IndustryRepository industryRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;


    public Page<CompanyDTO.ListCompanyResponse> findCompaniesFilter(CompanyFilter companyFilter,Pageable pageable) {
        Specification<Company> companySpecification = Specification.where(null);
        if (companyFilter.getName() != null) {
            companySpecification = companySpecification.and(CompanySpecification.containsName(companyFilter.getName()));
        }
        if (companyFilter.getLocation() != null) {
            companySpecification = companySpecification.and(CompanySpecification.containsLocation(companyFilter.getLocation()));
        }
        if (companyFilter.getIndustries() != null) {
            companySpecification = companySpecification.and(CompanySpecification.hasIndustries(companyFilter.getIndustries()));
        }
        if (companyFilter.getMinEmployees() != null || companyFilter.getMaxEmployees() != null) {
            companySpecification = companySpecification.and(CompanySpecification.inRangeEmployees(companyFilter.getMinEmployees(), companyFilter.getMaxEmployees()));
        }
        Page<Company> companies = companyRepository.findAll(companySpecification, pageable);
        return companies.map(companyMapper::toListCompanyResponse);
    }

    public CompanyDTO.CompanyResponse findCompanyDetail(Integer comapanyId) {
        return companyRepository.findById(comapanyId).map(companyMapper::toCompanyResponse).orElseThrow(
                () -> new ObjectNotFoundException("công ty", comapanyId)
        );
    }

    Company company;
    public CompanyDTO.CompanyResponse createUpdateCompany(CompanyDTO.CompanyRequest companyRequest, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getRole() != Role.RECRUITER) {
            throw new SharedException("Bạn không có quyền tạo hoặc cập nhật công ty");
        }
        if (companyRequest.id() == null) {
            if (user.getCompany() != null) {
                throw new SharedException("Người dùng đã có công ty");
            }
            company = new Company();
        } else {
            company = companyRepository.findById(companyRequest.id()).orElseThrow(
                    () -> new ObjectNotFoundException("Công ty", companyRequest.id())
            );
        }

        String logo = s3ImageUploader.upload(companyRequest.file());
        System.out.println(companyRequest.name());
        company.setLogo(logo);
        company.setName(companyRequest.name().split(",")[0]);
        company.setLocation(companyRequest.location().split(",")[0]);
        company.setEmployees(companyRequest.employees());
        company.setDescription(companyRequest.description());
        Set<Industry> industries = companyRequest.industries().stream()
                .map(industryRepository::findByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        company.setDateFounded(companyRequest.dateFounded());
        company.setIndustries(industries);
        company.setFacebookLink(companyRequest.facebookLink().split(",")[0]);
        company.setYoutubeLink(companyRequest.youtubeLink().split(",")[0]);
        company.setLinkedinLink(companyRequest.linkedinLink().split(",")[0]);
        company.setWebsiteLink(companyRequest.websiteLink().split(",")[0]);
        Company createdCompany = companyRepository.save(company);
        user.setCompany(createdCompany);
        userRepository.save(user);
        return companyMapper.toCompanyResponse(createdCompany);
    }

    public void addUserToCompany(Integer userId, String email) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getRole() != Role.RECRUITER) {
            throw new SharedException("Bạn không có quyền thêm người dùng vào công ty");
        }
        Company company = user.getCompany();

        User addedUser = userRepository.findUserByEmail(email).orElseThrow(
                () -> new SharedException("Không tìm thấy người dùng với email = " + email)
        );

        if (addedUser.getCompany() != null) {
            throw new SharedException("Người dùng này đã thộc công ty khác");
        }
        if (addedUser.getRole() != Role.RECRUITER) {
            throw new SharedException("Người dùng này không phải nhà tuyển dụng");
        }

        addedUser.setCompany(company);
        userRepository.save(addedUser);
    }

    public List<UserDTO.UserResponse> getUserInCompany(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        if (user.getRole() != Role.RECRUITER) {
            throw new SharedException("Bạn không có quyền xem nhân viên công ty");
        }
        Company company = user.getCompany();

        List<User> users = userRepository.findUsersByCompany(company);


        return users.stream().map(userMapper::toUserResponse).toList();

    }

    public Page<JobDTO.JobResponse> getApplicableJob(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("Người dùng", userId)
        );
        Company company = user.getCompany();
        Specification<Job> jobSpecification = Specification.where(null);
        jobSpecification = jobSpecification.and(JobSpecification.canApplyByCompany(company));
        Page<Job> jobPageable = jobRepository.findAll(jobSpecification, pageable);
        return jobPageable.map(jobMapper::toJobResponse);
    }
}
