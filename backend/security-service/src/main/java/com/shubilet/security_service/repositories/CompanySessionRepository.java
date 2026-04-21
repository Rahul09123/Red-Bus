package com.shubilet.security_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.security_service.models.CompanySession;

import jakarta.transaction.Transactional;

@Repository
public interface CompanySessionRepository extends JpaRepository<CompanySession, Integer> {

    /**

        Operation: Lookup

        Determines whether a company session exists that uses the specified session code.
        This lookup is typically performed during session creation to ensure code
        uniqueness and prevent collisions with existing session identifiers.

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
        FROM CompanySession s
        WHERE s.code = :code
    """)
    boolean hasCode(@Param("code") String code);

    /***

        Operation: ExistsByCompanyId

        Checks whether at least one active session exists for the specified company identifier.
        This method is typically used during authentication or session-creation workflows to
        prevent a company account from having multiple concurrent active sessions.

        The query evaluates the existence of {@link CompanySession} records by returning a
        boolean result derived from a count-based JPQL expression.

        <p>

            Usage:

            <pre>
                boolean hasActiveSession =
                    companySessionRepository.existsByCompanyId(10);
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CompanySession} as the underlying JPA entity</li>
                <li>{@link Query} for defining a custom JPQL existence check</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param companyId the unique identifier of the company to check for active sessions

        @return {@code true} if at least one session exists for the given companyId,
        otherwise {@code false}
    */
    @Query("""
        select count(a) > 0
        from CompanySession a
        where a.companyId = :companyId
    """)
    boolean existsByCompanyId(@Param("companyId") int companyId);

    /**

        Operation: Lookup

        Checks whether a company session exists that matches both the provided company
        identifier and session code. This operation is commonly used during session
        validation workflows to ensure that the session token belongs to the correct
        company account.

        <p>

            Uses:

            <ul>
                <li>JPA query for session existence verification</li>
            </ul>

        </p>

        @param companyId the identifier of the company to whom the session should belong

        @param code the session code associated with the session

        @return {@code true} if a matching session exists, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM CompanySession s
        WHERE s.companyId = :companyId
            AND s.code = :code
    """)
    boolean existsByCompanyIdAndCode(
            @Param("companyId") int companyId, 
            @Param("code") String code
    );

    /**

        Operation: Validate

        Determines whether a specific company session has expired by checking for a session
        record that matches both the provided company identifier and session code, and
        verifying that its expiration timestamp is less than or equal to the current time.
        This operation is typically used during authentication and session validation workflows
        to ensure that expired or invalid sessions are appropriately rejected.

        <p>

            Uses:

            <ul>
                <li>JPA query for session expiration evaluation</li>
            </ul>

        </p>

        @param companyId the identifier of the company whose session is being evaluated

        @param code the session code associated with the session

        @return {@code true} if the session has expired, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM CompanySession s
        WHERE s.companyId = :companyId
            AND s.code = :code
            AND s.expiresAt <= CURRENT_TIMESTAMP
    """)
    boolean isExpired(@Param("companyId") int companyId, @Param("code") String code);

    /**

        Operation: Cleanup

        Removes all company session records whose expiration timestamps have already passed.
        This bulk deletion helps maintain data integrity and prevents accumulation of stale
        session entries in the underlying {@code company_sessions} table. It is typically
        used in scheduled maintenance tasks or administrative cleanup workflows.

        <p>

            Uses:

            <ul>
                <li>Native SQL query for deleting expired session records</li>
                <li>Spring Transaction management for atomic operation execution</li>
            </ul>

        </p>

    */
    @Modifying
    @Transactional
    @Query(
        value = """
                DELETE FROM company_sessions
                WHERE expires_at < NOW()
                """,
        nativeQuery = true
    )
    void deleteExpiredSessions();

    /***

        Operation: DeleteByCompanyIdAndCode

        Deletes an active company session that matches the given company identifier and
        session authorization code. This method is primarily used during logout operations
        to invalidate a specific authenticated session belonging to a company.

        The operation executes within a transactional context to ensure data consistency
        and returns the number of records affected by the deletion.

        <p>

            Usage:

            <pre>
                int deletedCount =
                    companySessionRepository.deleteByCompanyIdAndCode(10, "XXXX-XXXX-XXXX-XXXX");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CompanySession} as the underlying JPA entity</li>
                <li>{@link JpaRepository} for derived delete query support</li>
                <li>{@link Transactional} to ensure atomic execution</li>
            </ul>

        </p>

        @param companyId the unique identifier of the company whose session will be deleted
        @param code the session authorization code associated with the company session

        @return the number of deleted session records (0 if none matched, 1 if deletion succeeded)
    */
    @Transactional
    int deleteByCompanyIdAndCode(int companyId, String code);

    /***

        Operation: FindByCompanyIdAndCode

        Retrieves an active company session that matches the given company identifier and
        session authorization code. This method is typically used during session validation
        and authentication checks to verify that a specific session exists and is valid.

        The result is wrapped in an {@link Optional} to explicitly handle the case where
        no matching session is found.

        <p>

            Usage:

            <pre>
                Optional<CompanySession> session =
                    companySessionRepository.findByCompanyIdAndCode(10, "XXXX-XXXX-XXXX-XXXX");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CompanySession} as the underlying JPA entity</li>
                <li>{@link JpaRepository} for derived query method support</li>
                <li>{@link Optional} for null-safe result handling</li>
            </ul>

        </p>

        @param companyId the unique identifier of the company
        @param code the session authorization code associated with the company session

        @return an {@link Optional} containing the matching {@link CompanySession} if found,
        or {@link Optional#empty()} if no session exists for the given parameters
    */
    Optional<CompanySession> findByCompanyIdAndCode(int companyId, String code);
}
