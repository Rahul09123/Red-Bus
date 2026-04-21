package com.shubilet.member_service.services.Impl;

import com.shubilet.member_service.dataTransferObjects.responses.MemberSessionDTO;
import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.repositories.AdminRepository;
import com.shubilet.member_service.repositories.CompanyRepository;
import com.shubilet.member_service.repositories.CustomerRepository;
import com.shubilet.member_service.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;

    public AuthServiceImpl(CustomerRepository customerRepository, CompanyRepository companyRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
    }

    public MemberSessionDTO checkMemberCredentials(String email, String password) {
        Object member;
        member = customerRepository.getCustomerByEmail(email);
        if (member != null) {
            Customer customer = (Customer) member;
            if (password.equals(customer.getPassword())) {
                return new MemberSessionDTO(customer.getId(), "CUSTOMER");
            }
            return null;
        }
        member = companyRepository.getCompanyByEmail(email);
        if (member != null) {
            Company company = (Company) member;
            if (password.equals(company.getPassword())) {
                if (!company.isVerified()) {
                    return new MemberSessionDTO("Company is not verified yet.");
                }
                return new MemberSessionDTO(company.getId(), "COMPANY");
            }
            return null;
        }
        member = adminRepository.getAdminByEmail(email);
        if (member != null) {
            Admin admin = (Admin) member;
            if (password.equals(admin.getPassword())) {
                if (admin.getRefAdminId() == null){
                    return new MemberSessionDTO("Admin is not verified yet.");
                }
                return new MemberSessionDTO(admin.getId(), "ADMIN");
            }
            return null;
        }
        return null;
    }

}
