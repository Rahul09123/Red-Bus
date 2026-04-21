package com.shubilet.member_service.initializers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.shubilet.member_service.models.Admin;
import com.shubilet.member_service.repositories.AdminRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminInitializer implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);
    
    private final AdminRepository adminRepository;

    public AdminInitializer(
        AdminRepository adminRepository
    ) {
        this.adminRepository = adminRepository;
    }

    /**
     * Initializes city-related data on application startup.
     * Currently empty.
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("CityInitializer run method called.");

        if(adminRepository.count() == 0) {
            logger.info("No admins found. Creating system administrator.");
            Admin admin = new Admin(
                "System",
                "Administrator",
                "shubilet@example.com",
                "SecurePassword123!"
            );

            admin.setRefAdminId(1);

            adminRepository.save(admin);
            logger.info("System administrator created and verified.");
        }
    }
}