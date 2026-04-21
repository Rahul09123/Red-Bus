package com.shubilet.member_service.services.Impl;

import com.shubilet.member_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs.CompanyIdDTO;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.repositories.CompanyRepository;
import com.shubilet.member_service.repositories.CustomerRepository;
import com.shubilet.member_service.services.ResourceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public ResourceServiceImpl(CustomerRepository customerRepository, CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    }

    public HashMap<Integer, String> sendCompanyNames(List<CompanyIdDTO> companyIdDTOS) {
        List<Integer> companyIds = new  LinkedList<>();
        for (CompanyIdDTO companyIdDTO : companyIdDTOS) {
            companyIds.add(companyIdDTO.getCompanyId());
        }
        List<Company> companies = companyRepository.getCompaniesByIdIn(companyIds);
        if (companies == null || companies.isEmpty()) {
            return null;
        }
        HashMap<Integer, String> res = new HashMap<>();

        for (Company company : companies) {
            res.put(company.getId(), company.getName());
        }
        return res;
    }

    public HashMap<Integer, String> sendCustomerNames(List<CustomerIdDTO> customerIDsDTO) {
        List<Integer> customerIds = new LinkedList<>();
        for (CustomerIdDTO customerIdDTO : customerIDsDTO) {
            customerIds.add(customerIdDTO.getCustomerId());
        }
        List<Customer> customers = customerRepository.getCustomersByIdIn(customerIds);
        if (customers == null || customers.isEmpty()) {
            return null;
        }
        HashMap<Integer, String> res = new HashMap<>();

        for (Customer customer : customers) {
            res.put(customer.getId(), customer.getName());
        }
        return res;
    }
}
