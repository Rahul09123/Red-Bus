package com.shubilet.member_service.services.Impl;

import org.springframework.stereotype.Service;

import com.shubilet.member_service.services.RegistrationService;

import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.models.Admin;

import com.shubilet.member_service.repositories.CustomerRepository;
import com.shubilet.member_service.repositories.CompanyRepository;
import com.shubilet.member_service.repositories.AdminRepository;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;

    public RegistrationServiceImpl(CustomerRepository customerRepository, CompanyRepository companyRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
    }

    public boolean registerCustomer(Customer customer) {
        customerRepository.save(customer);
        return true;
    }

    public boolean registerCompany(Company company) {
        companyRepository.save(company);
        return true;
    }

    public boolean registerAdmin(Admin admin) {
        adminRepository.save(admin);
        return true;
    }

    public boolean isUserExistsByEmail(String email) {
        return (customerRepository.isCustomerExistsByEmail(email) ||
                companyRepository.isCompanyExistsByEmail(email) ||
                adminRepository.isAdminExistsByEmail(email));

    }
}
