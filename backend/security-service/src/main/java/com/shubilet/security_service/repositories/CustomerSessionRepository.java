package com.shubilet.security_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.security_service.models.CustomerSession;

import jakarta.transaction.Transactional;

@Repository
public interface CustomerSessionRepository extends JpaRepository<CustomerSession, Integer> {

    /**

        Operation: Lookup

        Determines whether a customer session exists that uses the specified session code.
        This query checks the persistence layer to ensure uniqueness of session codes,
        typically during session creation workflows to avoid collisions with existing
        sessions.

        <p>

            Uses:

            <ul>
                <li>JPA query for session code existence checking</li>
            </ul>

        </p>

        @param code the session code whose existence is being checked

        @return {@code true} if a session with the given code exists, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM CustomerSession s
        WHERE s.code = :code
    """)
    boolean hasCode(@Param("code") String code);

    /***

        Operation: ExistsByCustomerId

        Checks whether an active session exists for the specified customer identifier.
        This method is typically used to prevent multiple concurrent sessions for the
        same customer or to quickly verify session presence without loading the full
        {@link CustomerSession} entity.

        The query returns a boolean result by evaluating the existence of at least one
        matching {@link CustomerSession} record for the given customerId.

        <p>

            Usage:

            <pre>
                boolean hasActiveSession =
                    customerSessionRepository.existsByCustomerId(25);
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CustomerSession} as the underlying JPA entity</li>
                <li>{@link Query} for custom JPQL execution</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param customerId the unique identifier of the customer whose session existence
        will be checked

        @return {@code true} if an active session exists for the given customerId,
        otherwise {@code false}
    */
    @Query("""
        select count(a) > 0
        from CustomerSession a
        where a.customerId = :customerId
    """)
    boolean existsByCustomerId(@Param("customerId") int customerId);

    /**

        Operation: Lookup

        Checks whether a customer session exists that matches both the specified customer
        identifier and session code. This operation is commonly used during session
        validation workflows to ensure that the provided token belongs to the correct
        customer.

        <p>

            Uses:

            <ul>
                <li>JPA query for session existence verification</li>
            </ul>

        </p>

        @param customerId the identifier of the customer to whom the session should belong

        @param code the session code associated with the session

        @return {@code true} if a matching session exists, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM CustomerSession s
        WHERE s.customerId = :customerId
            AND s.code = :code
    """)
    boolean existsByCustomerIdAndCode(
            @Param("customerId") int customerId, 
            @Param("code") String code
    );

    /**

        Operation: Validate

        Determines whether a customer session has expired by checking for a matching session
        record associated with the given customer identifier and session code, and confirming
        that its expiration timestamp is less than or equal to the current system time.
        This validation is typically used during session authentication to reject stale or
        invalid session tokens.

        <p>

            Uses:

            <ul>
                <li>JPA query for session expiration evaluation</li>
            </ul>

        </p>

        @param customerId the identifier of the customer whose session is being evaluated

        @param code the session code associated with the session

        @return {@code true} if the session has expired, otherwise {@code false}
    */
    @Query("""
        SELECT COUNT(s) > 0
        FROM CustomerSession s
        WHERE s.customerId = :customerId
            AND s.code = :code
            AND s.expiresAt <= CURRENT_TIMESTAMP
    """)
    boolean isExpired(@Param("customerId") int customerId, @Param("code") String code);

    /**

        Operation: Cleanup

        Deletes all customer session records whose expiration timestamps have already passed.
        This bulk cleanup operation helps maintain data integrity and prevents the persistence
        of stale or invalid session entries in the underlying {@code customer_sessions} table.
        It is commonly triggered by scheduled maintenance tasks or administrative actions.

        <p>

            Uses:

            <ul>
                <li>Native SQL query for removing expired session records</li>
                <li>Spring Transaction management for ensuring atomic execution</li>
            </ul>

        </p>

    */
    @Modifying
    @Transactional
    @Query(
        value = """
                DELETE FROM customer_sessions
                WHERE expires_at < NOW()
                """,
        nativeQuery = true
    )
    void deleteExpiredSessions();

    /***

        Operation: DeleteByCustomerIdAndCode

        Deletes an active customer session that matches the given customer identifier
        and session authorization code. This method is typically invoked during logout
        operations to invalidate a specific customer session in a transactional context.

        The return value represents the number of rows affected by the delete operation,
        allowing callers to determine whether a session was successfully removed.

        <p>

            Usage:

            <pre>
                int deletedCount =
                    customerSessionRepository.deleteByCustomerIdAndCode(25, "ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CustomerSession} as the underlying JPA entity</li>
                <li>{@link JpaRepository} for derived delete query support</li>
                <li>{@link Transactional} to ensure atomic delete behavior</li>
            </ul>

        </p>

        @param customerId the unique identifier of the customer whose session will be deleted
        @param code the session authorization code associated with the customer session

        @return the number of deleted session records (0 if no matching session exists)
    */
    @Transactional
    int deleteByCustomerIdAndCode(int customerId, String code);

    /***

        Operation: FindByCustomerIdAndCode

        Retrieves an active customer session that matches the provided customer identifier
        and session authorization code. This method is commonly used during session
        validation and authentication flows to confirm that a specific customer session
        exists and is still valid.

        The result is wrapped in an {@link Optional} to explicitly represent the possibility
        that no matching session is found.

        <p>

            Usage:

            <pre>
                Optional<CustomerSession> session =
                    customerSessionRepository.findByCustomerIdAndCode(25, "ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CustomerSession} as the underlying JPA entity</li>
                <li>{@link JpaRepository} for derived query method support</li>
                <li>{@link Optional} for null-safe result handling</li>
            </ul>

        </p>

        @param customerId the unique identifier of the customer
        @param code the session authorization code associated with the customer session

        @return an {@link Optional} containing the matching {@link CustomerSession} if found,
        or {@link Optional#empty()} if no session exists for the given parameters
    */
    Optional<CustomerSession> findByCustomerIdAndCode(int customerId, String code);
}
