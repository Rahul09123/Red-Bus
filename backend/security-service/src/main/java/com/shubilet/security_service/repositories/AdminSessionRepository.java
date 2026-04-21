package com.shubilet.security_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.security_service.models.AdminSession;

import jakarta.transaction.Transactional;

@Repository
public interface AdminSessionRepository extends JpaRepository<AdminSession, Integer> {

    /**

        Operation: Lookup

        Determines whether an existing admin session uses the specified session code.
        This operation checks the persistence layer for code uniqueness and is typically
        used during session creation workflows to prevent collisions between generated
        session identifiers.

        <p>

            Uses:

            <ul>
                <li>JPA query for session code existence checking</li>
            </ul>

        </p>

        @param code the session code whose existence is being verified

        @return {@code true} if a session with the given code exists, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM AdminSession s
        WHERE s.code = :code
    """)
    boolean hasCode(@Param("code") String code);

    /***

        Operation: ExistsByAdminId

        Checks whether at least one active session exists for the specified admin identifier.
        This query is commonly used to prevent multiple concurrent sessions for the same admin
        account, such as during login flows where only a single active session is allowed.

        The method leverages a JPQL query that evaluates the existence of records by returning
        a boolean result based on the count of matching {@link AdminSession} entities.

        <p>

            Usage:

            <pre>
                boolean hasActiveSession =
                    adminSessionRepository.existsByAdminId(5);
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link AdminSession} as the underlying JPA entity</li>
                <li>{@link Query} for defining a custom JPQL existence check</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param adminId the unique identifier of the admin to check for active sessions

        @return {@code true} if at least one session exists for the given adminId,
        otherwise {@code false}
    */
    @Query("""
        select count(a) > 0
        from AdminSession a
        where a.adminId = :adminId
    """)
    boolean existsByAdminId(@Param("adminId") int adminId);


    /**

        Operation: Lookup

        Checks whether an admin session exists that matches both the specified admin
        identifier and session code. This operation is commonly used during session
        validation workflows to ensure that the provided session token belongs to the
        correct administrator.

        <p>

            Uses:

            <ul>
                <li>JPA query for session existence verification</li>
            </ul>

        </p>

        @param adminId the identifier of the admin to whom the session should belong

        @param code the session code associated with the session

        @return {@code true} if a matching session exists, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM AdminSession s
        WHERE s.adminId = :adminId
            AND s.code = :code
    """)
    boolean existsByAdminIdAndCode(
            @Param("adminId") int adminId, 
            @Param("code") String code
    );

    /**

        Operation: Validate

        Determines whether a specific admin session has expired by checking for a session
        record that matches both the provided admin identifier and session code while
        verifying that its expiration timestamp is less than or equal to the current time.
        This evaluation is typically used during authentication and session validation
        processes to ensure that stale or invalid sessions are rejected.

        <p>

            Uses:

            <ul>
                <li>JPA query for session expiration evaluation</li>
            </ul>

        </p>

        @param adminId the identifier of the admin whose session is being evaluated

        @param code the session code associated with the session

        @return {@code true} if the session has expired, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM AdminSession s
        WHERE s.adminId = :adminId
            AND s.code = :code
            AND s.expiresAt <= CURRENT_TIMESTAMP
    """)
    boolean isExpired(@Param("adminId") int adminId, @Param("code") String code);

    /**

        Operation: Cleanup

        Deletes all admin session records from the underlying storage whose expiration
        timestamps have passed. This operation is typically invoked as part of scheduled
        maintenance tasks or administrative cleanup workflows to remove stale or invalid
        session entries and preserve data integrity.

        <p>

            Uses:

            <ul>
                <li>Native SQL query for bulk deletion of expired session records</li>
                <li>Spring Transaction management for ensuring atomic execution</li>
            </ul>

        </p>

    */
    @Modifying
    @Transactional
    @Query(
        value = """
                DELETE FROM admin_sessions
                WHERE expires_at < NOW()
                """,
        nativeQuery = true
    )
    void deleteExpiredSessions();

    /***

        Operation: DeleteSession

        Deletes an active admin session by matching the given admin identifier and session
        authorization code. This operation is executed within a transactional context to
        ensure atomic removal of the session record from the persistence layer. It is typically
        invoked during logout or session invalidation flows.

        <p>

            Usage:

            <pre>
                deleteByAdminIdAndCode(5, "ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link AdminSession} as the underlying JPA entity</li>
                <li>{@link JpaRepository} for data access abstraction</li>
                <li>{@link Transactional} to ensure transactional delete semantics</li>
            </ul>

        </p>

        @param adminId the unique identifier of the admin whose session will be deleted
        @param code the authorization/session code associated with the admin session

        @return the number of session records deleted (0 if none matched, 1 if deletion succeeded)
    */
    @Transactional
    int deleteByAdminIdAndCode(int adminId, String code);

    /***

        Operation: FindSession

        Retrieves an admin session by matching the provided admin identifier and session
        authorization code. This method is used to validate whether an active session exists
        for the given admin context and to inspect session state before performing operations
        such as validation, renewal, or deletion.

        <p>

            Usage:

            <pre>
                Optional<AdminSession> session =
                    adminSessionRepository.findByAdminIdAndCode(5, "ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link AdminSession} as the underlying JPA entity</li>
                <li>{@link Optional} to safely represent presence or absence of a session</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param adminId the unique identifier of the admin whose session is being queried
        @param code the authorization/session code associated with the admin session

        @return an {@link Optional} containing the {@link AdminSession} if found,
        or {@link Optional#empty()} if no matching session exists
    */
    Optional<AdminSession> findByAdminIdAndCode(int adminId, String code);
}
