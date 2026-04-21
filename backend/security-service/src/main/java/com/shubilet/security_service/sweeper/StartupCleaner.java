package com.shubilet.security_service.sweeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.shubilet.security_service.controllers.Impl.AuthControllerImpl;
import com.shubilet.security_service.services.AdminSessionService;
import com.shubilet.security_service.services.CompanySessionService;
import com.shubilet.security_service.services.CustomerSessionService;


/**

    Domain: Startup

    Executes a cleanup routine during application startup to ensure that no stale or
    inconsistent session records persist from previous runs. This component implements
    {@link CommandLineRunner} to trigger early in the application lifecycle, invoking the
    session cleanup operations of the admin, company, and customer session services.
    It provides a centralized and automated mechanism for resetting session state,
    improving system consistency and preventing authentication anomalies.

    <p>

        Technologies:

        <ul>
            <li>Spring Boot {@code CommandLineRunner} for startup execution</li>
            <li>Spring Component model</li>
            <li>SLF4J for startup logging</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@Component
public class StartupCleaner implements CommandLineRunner{
    private static final Logger logger = LoggerFactory.getLogger(AuthControllerImpl.class);

    private final AdminSessionService adminSessionService;
    private final CompanySessionService companySessionService;
    private final CustomerSessionService customerSessionService;

    public StartupCleaner(
        AdminSessionService adminSessionService, 
        CompanySessionService companySessionService, 
        CustomerSessionService customerSessionService
    ) {
        this.adminSessionService = adminSessionService;
        this.companySessionService = companySessionService;
        this.customerSessionService = customerSessionService;
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Cleaning up all sessions on startup...");
        adminSessionService.cleanAllSessions();
        companySessionService.cleanAllSessions();
        customerSessionService.cleanAllSessions();
        logger.info("All sessions cleaned up.");
    }

    // Mirliva says: Fresh start. No geçmiş, no drama, no session.
}
