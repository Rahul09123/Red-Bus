package com.shubilet.expedition_service.repositories;

import java.util.List;
import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCompanyRepoDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCustomerRepoDTO;
import com.shubilet.expedition_service.models.City;
import com.shubilet.expedition_service.models.Expedition;


@Repository
public interface ExpeditionRepository extends JpaRepository<Expedition, Integer> {

    /***

        Operation: FindAllByCompanyId

        Retrieves all expeditions belonging to the specified company, ordered by
        their scheduled date and time in ascending order. This method performs
        a join between {@link Expedition} and {@link City} entities to resolve
        human-readable departure and arrival city names and maps the result
        directly into {@link ExpeditionForCompanyRepoDTO} projection objects.

        The query is optimized for read-only, company-scoped expedition listing
        use cases such as dashboards, reporting, and management views, and avoids
        loading full entity graphs by using a constructor expression.

        <p>

            Usage:

            <pre>
                List&lt;ExpeditionForCompanyRepoDTO&gt; expeditions =
                    expeditionRepository.findAllByCompanyId(5);
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Expedition} as the primary JPA entity</li>
                <li>{@link City} for resolving departure and arrival city names</li>
                <li>{@link ExpeditionForCompanyRepoDTO} as a projection DTO</li>
                <li>{@link Query} for custom JPQL constructor expression</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param companyId the identifier of the company whose expeditions will be retrieved

        @return a list of {@link ExpeditionForCompanyRepoDTO} representing all expeditions
        owned by the specified company, ordered by date and time
    */
    @Query("""
    SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCompanyRepoDTO(
            e.id,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.price,
            e.duration,
            e.capacity,
            e.numberOfBookedSeats,
            e.profit
        )
        FROM Expedition e
            JOIN City dc 
                ON e.departureCityId = dc.id
                    JOIN City ac 
                        ON e.arrivalCityId = ac.id
        WHERE e.companyId = :companyId
        ORDER BY e.dateAndTime ASC
    """)
    List<ExpeditionForCompanyRepoDTO> findAllByCompanyId(@Param("companyId") int companyId);

    /***

        Operation: FindUpcomingExpeditions

        Retrieves all upcoming expeditions belonging to the specified company whose
        scheduled date and time is greater than or equal to the provided reference
        instant. This method joins {@link Expedition} with {@link City} entities to
        resolve departure and arrival city names and maps the result set directly
        into {@link ExpeditionForCompanyRepoDTO} projection objects.

        The query is designed for company dashboards and operational views where
        only future (active) expeditions are relevant, and results are ordered
        chronologically by their scheduled date and time.

        <p>

            Usage:

            <pre>
                List&lt;ExpeditionForCompanyRepoDTO&gt; expeditions =
                    expeditionRepository.findUpcomingExpeditions(
                        companyId,
                        Instant.now()
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Expedition} as the primary JPA entity</li>
                <li>{@link City} for resolving departure and arrival city names</li>
                <li>{@link ExpeditionForCompanyRepoDTO} as a projection DTO</li>
                <li>{@link Instant} for temporal filtering of future expeditions</li>
                <li>{@link Query} for custom JPQL constructor expression</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param companyId the identifier of the company whose upcoming expeditions will be retrieved

        @param now the reference instant used to filter expeditions scheduled in the future

        @return a list of {@link ExpeditionForCompanyRepoDTO} representing upcoming expeditions
        for the specified company, ordered by date and time
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCompanyRepoDTO(
            e.id,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.price,
            e.duration,
            e.capacity,
            e.numberOfBookedSeats,
            e.profit
        )
        FROM Expedition e
            JOIN City dc 
                ON e.departureCityId = dc.id
                    JOIN City ac 
                        ON e.arrivalCityId = ac.id
                            WHERE e.companyId = :companyId
                                AND e.dateAndTime >= :now
        ORDER BY e.dateAndTime ASC
    """)
    List<ExpeditionForCompanyRepoDTO> findUpcomingExpeditions(
            @Param("companyId") int companyId,
            @Param("now") Instant now
    );

    /***

        Operation: FindByInstantAndRoute

        Retrieves all available expeditions for customers filtered by departure city,
        arrival city, and a specific date range. This method returns only expeditions
        that still have available capacity (i.e., booked seats are less than total
        capacity) and whose scheduled date and time fall within the provided start
        and end instants of the target day.

        The query resolves human-readable city names by joining {@link Expedition}
        with {@link City} entities and maps results directly into
        {@link ExpeditionForCustomerRepoDTO} projection objects, optimized for
        customer-facing expedition search scenarios.

        <p>

            Usage:

            <pre>
                List&lt;ExpeditionForCustomerRepoDTO&gt; expeditions =
                    expeditionRepository.findByInstantAndRoute(
                        departureCityId,
                        arrivalCityId,
                        startOfDay,
                        endOfDay
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Expedition} as the primary JPA entity</li>
                <li>{@link City} for resolving departure and arrival city names</li>
                <li>{@link ExpeditionForCustomerRepoDTO} as a projection DTO</li>
                <li>{@link Instant} for date-based filtering</li>
                <li>{@link Query} for custom JPQL constructor expression</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param departureCityId the identifier of the departure city

        @param arrivalCityId the identifier of the arrival city

        @param startOfDay the start instant of the target date (inclusive)

        @param endOfDay the end instant of the target date (exclusive)

        @return a list of {@link ExpeditionForCustomerRepoDTO} representing available
        expeditions for the given route and date, ordered by date and time
    */
    @Query("""
    SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCustomerRepoDTO(
            e.id,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.price,
            e.duration,
            e.companyId
        )
        FROM Expedition e, City dc, City ac
        WHERE e.departureCityId = dc.id
            AND e.arrivalCityId = ac.id
            AND e.departureCityId = :departureCityId
            AND e.arrivalCityId = :arrivalCityId
            AND e.dateAndTime >= :startOfDay
            AND e.dateAndTime < :endOfDay
            AND e.capacity > e.numberOfBookedSeats
        ORDER BY e.dateAndTime ASC
    """)
    List<ExpeditionForCustomerRepoDTO> findByInstantAndRoute(
            @Param("departureCityId") int departureCityId,
            @Param("arrivalCityId") int arrivalCityId,
            @Param("startOfDay") Instant startOfDay,
            @Param("endOfDay") Instant endOfDay
    );

    /***

        Operation: FindAllByInstantAndCompanyId

        Retrieves all expeditions belonging to a specific company that are scheduled
        within the given date range. The method filters expeditions by the provided
        start and end instants (typically representing a single day), resolves
        departure and arrival city names via {@link City} joins, and maps results
        directly into {@link ExpeditionForCompanyRepoDTO} projection objects.

        This query is intended for company-side views where expeditions need to be
        listed for a particular date, including detailed business metrics such as
        capacity, booked seat count, and generated profit, ordered chronologically.

        <p>

            Usage:

            <pre>
                List&lt;ExpeditionForCompanyRepoDTO&gt; expeditions =
                    expeditionRepository.findAllByInstantAndCompanyId(
                        startOfDay,
                        endOfDay,
                        companyId
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Expedition} as the primary JPA entity</li>
                <li>{@link City} for resolving departure and arrival city names</li>
                <li>{@link ExpeditionForCompanyRepoDTO} as a projection DTO</li>
                <li>{@link Instant} for date-range filtering</li>
                <li>{@link Query} for custom JPQL constructor expression</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param startOfDay the start instant of the target date range (inclusive)

        @param endOfDay the end instant of the target date range (exclusive)

        @param companyId the identifier of the company whose expeditions will be retrieved

        @return a list of {@link ExpeditionForCompanyRepoDTO} representing expeditions
        scheduled within the given date range for the specified company, ordered by date and time
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.ExpeditionForCompanyRepoDTO(
            e.id,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.price,
            e.duration,
            e.capacity,
            e.numberOfBookedSeats,
            e.profit
        )
        FROM Expedition e
            JOIN City dc 
                ON e.departureCityId = dc.id
                    JOIN City ac 
                        ON e.arrivalCityId = ac.id
                            WHERE e.dateAndTime >= :startOfDay
                                AND e.dateAndTime < :endOfDay
                                AND e.companyId = :companyId
        ORDER BY e.dateAndTime ASC
    """)
    List<ExpeditionForCompanyRepoDTO> findAllByInstantAndCompanyId(
        @Param("startOfDay") Instant startOfDay, 
        @Param("endOfDay") Instant endOfDay,
        @Param("companyId") int companyId
    );

    /***

        Operation: IsExpeditionTimePassed

        Determines whether the scheduled date and time of a given expedition has
        already passed relative to the provided reference instant. This method is
        typically used during reservation and validation workflows to prevent
        ticket purchases or seat reservations for expeditions that are no longer
        valid due to being in the past.

        The query evaluates the {@code dateAndTime} field of the {@link Expedition}
        entity and returns a boolean result without loading the full entity.

        <p>

            Usage:

            <pre>
                boolean isPast =
                    expeditionRepository.isExpeditionTimePassed(
                        expeditionId,
                        Instant.now()
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Expedition} as the underlying JPA entity</li>
                <li>{@link Instant} for time comparison</li>
                <li>{@link Query} for custom JPQL boolean evaluation</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param expeditionId the identifier of the expedition to evaluate

        @param now the reference instant used to determine whether the expedition time has passed

        @return {@code true} if the expedition date and time is earlier than the given instant,
        otherwise {@code false}
    */
    @Query("""
        SELECT CASE 
            WHEN e.dateAndTime < :now THEN true
            ELSE false
        END
        FROM Expedition e
        WHERE e.id = :expeditionId
    """)
    boolean isExpeditionTimePassed(
        @Param("expeditionId") int expeditionId, 
        @Param("now") Instant now
    );
}