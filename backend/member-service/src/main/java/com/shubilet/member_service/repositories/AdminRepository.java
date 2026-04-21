package com.shubilet.member_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.member_service.models.Admin;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query(
            value = """
                    SELECT COUNT(*) > 0
                    FROM Admin a
                    WHERE a.email = :email
                    """
    )
    boolean isAdminExistsByEmail(
            @Param("email") String email
    );

    Admin getAdminByEmail(String email);

    Admin getAdminById(int id);

    boolean existsAdminById(int id);

    List<Admin> getAdminByRefAdminIdIsNull();
}