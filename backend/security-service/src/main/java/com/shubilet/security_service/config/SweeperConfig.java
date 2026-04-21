package com.shubilet.security_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**

    Domain: Scheduling

    Provides the configuration entry point for enabling scheduled tasks within the application.
    By applying the {@link EnableScheduling} annotation, this configuration class activates
    Spring’s scheduling subsystem, allowing components such as sweepers or cleanup routines
    to run at fixed intervals or cron-based schedules. The class is intentionally minimal,
    serving solely as a trigger for scheduling capabilities.

    <p>

        Technologies:

        <ul>
            <li>Spring Framework Configuration</li>
            <li>Spring Scheduling ({@code @EnableScheduling})</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMirliva

    @version 1.0
*/
@Configuration
@EnableScheduling
public class SweeperConfig {
    // No code needed here, @EnableScheduling is enough.
}
