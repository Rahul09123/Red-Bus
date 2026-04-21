package com.shubilet.member_service.services.Impl;

import com.shubilet.member_service.common.enums.Gender;
import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.models.FavoriteCompany;
import com.shubilet.member_service.repositories.AdminRepository;
import com.shubilet.member_service.repositories.CompanyRepository;
import com.shubilet.member_service.repositories.CustomerRepository;
import com.shubilet.member_service.repositories.FavoriteCompanyRepository;
import com.shubilet.member_service.services.ProfileService;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final FavoriteCompanyRepository favoriteCompanyRepository;

    public ProfileServiceImpl(CustomerRepository customerRepository, CompanyRepository companyRepository, AdminRepository adminRepository, FavoriteCompanyRepository favoriteCompanyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.favoriteCompanyRepository = favoriteCompanyRepository;

    }

    public boolean isCustomerExists(int customerId) {
        return customerRepository.existsById(customerId);
    }

    public boolean isCompanyExists(int companyId) {
        return companyRepository.existsById(companyId);
    }

    public boolean isRelationExists(int relationId) {return favoriteCompanyRepository.existsById(relationId);}

    public boolean editName(int customerId, String name) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setName(name);
        customerRepository.save(customer);
        return true;
    }

    public boolean editSurname(int customerId, String surname) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setSurname(surname);
        customerRepository.save(customer);
        return true;
    }

    public boolean editGender(int customerId, Gender gender) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setGender(gender);
        customerRepository.save(customer);
        return true;
    }

    public boolean editEmail(int customerId, String email) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setEmail(email);
        customerRepository.save(customer);
        return true;
    }

    public boolean editPassword(int customerId, String password) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        customer.setPassword(password);
        customerRepository.save(customer);
        return true;
    }

    public boolean addFavoriteCompany(FavoriteCompany favoriteCompany) {
        favoriteCompanyRepository.save(favoriteCompany);
        return true;
    }

    public boolean deleteFavoriteCompany(int relationId) {
        favoriteCompanyRepository.deleteById(relationId);
        return true;
    }
    public Customer getCustomerById(int customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
    public Company getCompanyById(int companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    public Admin getAdminById(int adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }
    
}
