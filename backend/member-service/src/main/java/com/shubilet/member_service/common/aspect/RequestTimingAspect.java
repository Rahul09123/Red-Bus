package com.shubilet.member_service.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**

    Domain: Monitoring

    Provides cross-cutting performance monitoring for controller and sweeper components by
    leveraging Spring AOP. This aspect intercepts method executions within defined packages,
    measures their runtime, and logs structured performance data without affecting the core
    business logic. It serves as a centralized mechanism for lightweight profiling and can
    assist in identifying bottlenecks, slow endpoints, or unexpected execution patterns
    across the application.

    <p>

        Technologies:

        <ul>
            <li>Spring AOP for method interception</li>
            <li>SLF4J for performance logging</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@Aspect
@Component
public class RequestTimingAspect {

    private static final Logger log = LoggerFactory.getLogger(RequestTimingAspect.class);

    /**

        Operation: Monitor

        Measures and logs the execution time of controller and sweeper-layer methods by wrapping
        their invocation with an AOP around advice. The method records the start and end timestamps,
        calculates the duration, and emits a performance log entry identifying the fully qualified
        method name and total execution time. This provides lightweight runtime profiling without
        altering business logic or controller implementations.

        <p>

            Uses:

            <ul>
                <li>{@code ProceedingJoinPoint} for invoking the intercepted method and accessing metadata</li>
                <li>{@code log} for emitting structured performance metrics</li>
                <li>Spring AOP interception to measure execution of controllers and sweeper components</li>
            </ul>

        </p>

        @param joinPoint the join point representing the intercepted method invocation

        @return the result of the intercepted method after execution completes

        @throws Throwable if the intercepted method throws any exception during execution
    */
    @Around("""
            execution(* com.shubilet.member_service.controllers..*(..)) ||
            execution(* com.shubilet.member_service.sweeper..*(..))
            """)
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // method actually runs here

        long end = System.currentTimeMillis();

        long duration = end - start;

        log.info("[PERF] {}.{}() took {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    duration);

        return result;
    }
}
