package com.shubilet.expedition_service.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.SeatForCompanyRepoDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.SeatForCustomerRepoDTO;
import com.shubilet.expedition_service.models.Expedition;
import com.shubilet.expedition_service.models.Seat;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    
    /***

        Operation: FindSeatsByExpeditionIdAndCompanyId

        Retrieves detailed seat information for a specific expedition belonging to the given
        company. This method joins {@link Seat} and {@link Expedition} entities to ensure
        company ownership validation and returns seat-level data ordered by seat number.
        The result is mapped directly into {@link SeatForCompanyRepoDTO} projection objects,
        providing a lightweight and efficient representation for company-facing views.

        This query is typically used in company dashboards to inspect seat occupancy,
        customer associations, and reservation status for a particular expedition.

        <p>

            Usage:

            <pre>
                List&lt;SeatForCompanyRepoDTO&gt; seats =
                    seatRepository.findSeatsByExpeditionIdAndCompanyId(
                        expeditionId,
                        companyId
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Seat} as the primary JPA entity</li>
                <li>{@link Expedition} for validating company ownership</li>
                <li>{@link SeatForCompanyRepoDTO} as a projection DTO</li>
                <li>{@link Query} for custom JPQL constructor expression</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param expeditionId the identifier of the expedition whose seats will be retrieved

        @param companyId the identifier of the company owning the expedition

        @return a list of {@link SeatForCompanyRepoDTO} representing all seats of the expedition,
        ordered by seat number
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.SeatForCompanyRepoDTO(
            s.id,
            s.expeditionId,
            s.seatNo,
            s.customerId,
            s.status
        )
        FROM Seat s
            JOIN Expedition e
                ON s.expeditionId = e.id
                    WHERE s.expeditionId = :expeditionId
                        AND e.companyId = :companyId
        ORDER BY s.seatNo ASC
    """)
    List<SeatForCompanyRepoDTO> findSeatsByExpeditionIdAndCompanyId(
        @Param("expeditionId") int expeditionId, 
        @Param("companyId") int companyId
    );

    /***

        Operation: ExistsByExpeditionIdAndSeatNo

        Checks whether a seat with the given seat number exists for the specified
        expedition. This method is commonly used during ticket purchase and seat
        reservation workflows to validate seat existence before proceeding with
        booking or availability checks.

        The query evaluates the presence of at least one {@link Seat} entity that
        matches both the expedition identifier and the seat number, returning a
        boolean result without loading the full entity.

        <p>

            Usage:

            <pre>
                boolean seatExists =
                    seatRepository.existsByExpeditionIdAndSeatNo(
                        expeditionId,
                        seatNo
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Seat} as the underlying JPA entity</li>
                <li>{@link Query} for custom JPQL existence check</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param expeditionId the identifier of the expedition

        @param seatNo the seat number to check for existence

        @return {@code true} if the seat exists for the given expedition,
        otherwise {@code false}
    */
    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
        FROM Seat s
        WHERE s.expeditionId = :expeditionId
            AND s.seatNo = :seatNo
    """)
    boolean existsByExpeditionIdAndSeatNo(
            @Param("expeditionId") int expeditionId,
            @Param("seatNo") int seatNo
    );

    /***

        Operation: FindByExpeditionIdAndSeatNo

        Retrieves the {@link Seat} entity associated with the specified expedition
        and seat number. This method is typically used during reservation, booking,
        or status update workflows where direct access to the seat entity is required
        after preliminary existence and availability validations have been completed.

        The query returns the matching {@link Seat} without additional joins and
        assumes that the combination of expeditionId and seatNo uniquely identifies
        a seat within the system.

        <p>

            Usage:

            <pre>
                Seat seat =
                    seatRepository.findByExpeditionIdAndSeatNo(
                        expeditionId,
                        seatNo
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Seat} as the managed JPA entity</li>
                <li>{@link Query} for custom JPQL entity retrieval</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param expeditionId the identifier of the expedition to which the seat belongs

        @param seatNo the seat number within the expedition

        @return the {@link Seat} entity matching the given expeditionId and seatNo
    */
    @Query("""
        SELECT s
        FROM Seat s
            WHERE  s.seatNo = :seatNo
                AND s.expeditionId = :expeditionId
        """)
    Seat findByExpeditionIdAndSeatNo(
            @Param("expeditionId") int expeditionId,
            @Param("seatNo") int seatNo
    );

    /***

        Operation: FindSeatsByExpeditionIdAndStatus

        Retrieves seat information for customer-facing views by returning all seats
        belonging to the specified expedition, provided that the expedition is still
        active (date has not passed) and has remaining capacity. The result is mapped
        into {@link SeatForCustomerRepoDTO} projections, exposing only the necessary
        seat details such as seat number and current reservation status.

        This method is designed for read-only customer scenarios (e.g., seat selection
        screens) and intentionally filters out past or fully booked expeditions to
        prevent invalid reservation attempts.

        <p>

            Usage:

            <pre>
                List&lt;SeatForCustomerRepoDTO&gt; seats =
                    seatRepository.findSeatsByExpeditionIdAndStatus(
                        expeditionId,
                        Instant.now()
                    );
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Seat} as the underlying JPA entity</li>
                <li>{@link Expedition} for expedition state filtering</li>
                <li>{@link SeatForCustomerRepoDTO} as a lightweight projection</li>
                <li>{@link Query} for custom JPQL selection</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link Instant} for time-based expedition validation</li>
            </ul>

        </p>

        @param expeditionId the identifier of the expedition whose seats will be retrieved

        @param now the current timestamp used to ensure the expedition has not yet started

        @return a list of {@link SeatForCustomerRepoDTO} representing seats available
        for customer visibility, ordered by seat number
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.SeatForCustomerRepoDTO(
            s.expeditionId,
            s.seatNo,
            s.status
        )
        FROM Seat s
        WHERE s.expeditionId IN (
                SELECT e.id 
                FROM Expedition e 
                WHERE e.dateAndTime >= :now
                    AND e.capacity > e.numberOfBookedSeats
            )
            AND s.expeditionId = :expeditionId
        ORDER BY s.seatNo ASC
    """)
    List<SeatForCustomerRepoDTO> findSeatsByExpeditionIdAndStatus(
            @Param("expeditionId") int expeditionId,
            @Param("now") Instant now
    );

}
