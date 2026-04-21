package com.shubilet.member_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.member_service.models.Company;

import java.util.Collection;
import java.util.List;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query(
            value = """
                    SELECT COUNT(*) > 0
                    FROM Company c
                    WHERE c.email = :email
                    """
    )
    boolean isCompanyExistsByEmail(
            @Param("email") String email
    );


    Company getCompanyByEmail(String email);


    List<Company> getCompaniesByIdIn(Collection<Integer> ids);

    List<Company> getCompanyById(int id);

    List<Company> getCompanyByIsVerified(boolean isVerified);
}
