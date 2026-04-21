package com.shubilet.member_service.services;


import com.shubilet.member_service.common.enums.Gender;
import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.models.FavoriteCompany;

public interface ProfileService {
    public boolean isCustomerExists(int customerId);

    public boolean isCompanyExists(int companyId);
    public boolean isRelationExists(int relationId);


    public boolean editName(int customerId, String name);

    public boolean editSurname(int customerId, String surname);

    public boolean editGender(int customerId, Gender gender);

    public boolean editEmail(int customerId, String email);

    public boolean editPassword(int customerId, String password);

    public boolean addFavoriteCompany(FavoriteCompany favoriteCompany);

    public boolean deleteFavoriteCompany(int relationId);

    public Customer getCustomerById(int customerId);

    public Company getCompanyById(int companyId);
    public Admin getAdminById(int adminId);


}
