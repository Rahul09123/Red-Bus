package com.shubilet.security_service.sweeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shubilet.security_service.common.constants.AppConstants;
import com.shubilet.security_service.services.AdminSessionService;
import com.shubilet.security_service.services.CompanySessionService;
import com.shubilet.security_service.services.CustomerSessionService;

/**

    Domain: Scheduling

    Provides an automated session maintenance component responsible for periodically
    sweeping and removing expired session records across all session domains
    (admin, company, and customer). Operating as part of the application's scheduled
    task infrastructure, this class ensures long-term data hygiene and prevents the
    accumulation of stale or invalid session entries. Leveraging Spring's scheduling
    capabilities, it triggers cleanup operations at intervals defined in the
    application constants while logging the lifecycle of each cleanup cycle.

    <p>

        Technologies:

        <ul>
            <li>Spring Scheduling</li>
            <li>Spring Component</li>
            <li>SLF4J Logging</li>
        </ul>
    </p>

    @see AdminSessionService

    @see CompanySessionService

    @see CustomerSessionService

    @version 1.0
*/
@Component
public class SessionSweeper {

    private static final Logger logger = LoggerFactory.getLogger(SessionSweeper.class);

    private final AdminSessionService adminSessionService;
    private final CompanySessionService companySessionService;
    private final CustomerSessionService customerSessionService;

    public SessionSweeper(
            AdminSessionService adminSessionService,
            CompanySessionService companySessionService,
            CustomerSessionService customerSessionService
    ) {
        this.adminSessionService = adminSessionService;
        this.companySessionService = companySessionService;
        this.customerSessionService = customerSessionService;
    }

    /**

        Operation: Cleanup

        Performs a scheduled cleanup routine that removes expired sessions across all
        admin, company, and customer session domains. This method is triggered at fixed
        intervals defined by the application constants and logs both the start and
        completion of the sweep. It ensures system hygiene by delegating cleanup tasks
        to the corresponding session services, preventing accumulation of stale session
        data.

        <p>

            Uses:

            <ul>
                <li>AdminSessionService for removing expired admin sessions</li>
                <li>CompanySessionService for removing expired company sessions</li>
                <li>CustomerSessionService for removing expired customer sessions</li>
                <li>Spring Scheduling for timed task execution</li>
            </ul>

        </p>

        @return nothing; performs cleanup as a scheduled side effect
    */
    @Scheduled(
        fixedDelayString = AppConstants.FIXED_DELAY_STRING,
        initialDelayString = AppConstants.INITIAL_DELAY_STRING
    )
    public void sweepExpiredSessions() {
        logger.info("SessionSweeper started - cleaning expired sessions...");

        adminSessionService.cleanExpiredSessions();
        companySessionService.cleanExpiredSessions();
        customerSessionService.cleanExpiredSessions();

        logger.info("SessionSweeper completed - session cleanup finished.");
    }

    // Mirliva says: Some sessions just need to be... cleaned.
    // No witnesses. No logs. Just peace.
}
