package com.shubilet.member_service.services;

import com.shubilet.member_service.models.Company;
import com.shubilet.member_service.models.Customer;
import com.shubilet.member_service.models.Admin;


public interface RegistrationService {
    /**
     * Creates a Customer on the Table with Validated Costumer Model
     *
     * @param customer Customer Model that going to be saved on DB
     * @return Returns True when operation is successful, false otherwise.
     */
    boolean registerCustomer(Customer customer);

    /**
     * Creates a Company on the Table with Validated Costumer Model
     *
     * @param company Company Model that going to be saved on DB
     * @return Returns True when operation is successful, false otherwise.
     */
    boolean registerCompany(Company company);

    /**
     * Creates an Admin on the Table with Validated Costumer Model
     *
     * @param admin Admin Model that going to be saved on DB
     * @return Returns True when operation is successful, false otherwise.
     */
    boolean registerAdmin(Admin admin);

    /**
     * Checks that is there any user with given email
     *
     * @param email Email value to be checked
     * @return Returns True if there are any customer with given email, False otherwise.
     */
    boolean isUserExistsByEmail(String email);
}
