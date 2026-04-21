package com.shubilet.expedition_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.TicketRepoDTO;
import com.shubilet.expedition_service.models.City;
import com.shubilet.expedition_service.models.Expedition;
import com.shubilet.expedition_service.models.Seat;
import com.shubilet.expedition_service.models.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    
    /***

        Operation: FindTicketDetailsByPNR

        Retrieves detailed ticket information for a specific ticket identified by its
        unique PNR (Passenger Name Record). This method joins ticket, seat, expedition,
        and city entities to construct a comprehensive, read-only {@link TicketRepoDTO}
        projection containing journey, seat, and expedition metadata.

        The query is optimized for ticket detail views and avoids returning full entity
        graphs by directly mapping the result into a repository-level DTO.

        <p>

            Usage:

            <pre>
                TicketRepoDTO ticket =
                    ticketRepository.findTicketDetailsByPNR("ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Ticket} as the primary ticket entity</li>
                <li>{@link Seat} to resolve seat number information</li>
                <li>{@link Expedition} for expedition and company details</li>
                <li>{@link City} for departure and arrival city names</li>
                <li>{@link TicketRepoDTO} as a lightweight projection for ticket details</li>
                <li>{@link Query} for custom JPQL constructor-based selection</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param PNR the unique Passenger Name Record identifying the ticket

        @return a {@link TicketRepoDTO} containing full ticket, seat, and expedition details
        associated with the given PNR
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.TicketRepoDTO(
            t.PNR,
            s.seatNo,
            e.id,
            e.companyId,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.duration
        )
        FROM Ticket t
            JOIN Seat s ON t.seatId = s.id
            JOIN Expedition e ON s.expeditionId = e.id
            JOIN City dc ON e.departureCityId = dc.id
            JOIN City ac ON e.arrivalCityId = ac.id
        WHERE t.PNR = :PNR
        """
    )
    TicketRepoDTO findTicketDetailsByPNR(
        @Param("PNR") String PNR
    );

    /***

        Operation: ExistsByPNR

        Checks whether a ticket with the specified PNR (Passenger Name Record) exists
        in the system. This method performs a lightweight existence check without
        fetching the full {@link Ticket} entity, making it suitable for validation
        and guard clauses prior to ticket-related operations.

        <p>

            Usage:

            <pre>
                boolean exists = ticketRepository.existsByPNR("ABC123XYZ");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Ticket} as the underlying persistence entity</li>
                <li>{@link Query} for custom JPQL-based existence check</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param PNR the Passenger Name Record to check for existence

        @return {@code true} if a ticket with the given PNR exists, otherwise {@code false}
    */
    @Query("""
        SELECT CASE 
            WHEN COUNT(t) > 0 THEN true
            ELSE false
        END
        FROM Ticket t
        WHERE t.PNR = :PNR
        """)
    boolean existsByPNR(String PNR);

    /***

        Operation: FindTicketsByCustomerId

        Retrieves all tickets owned by a specific customer by querying ticket,
        seat, expedition, and city data in a single projection query. This method
        returns enriched ticket details including route information, schedule,
        company ownership, and journey duration, without exposing persistence
        entities directly.

        <p>

            Usage:

            <pre>
                List<TicketRepoDTO> tickets =
                    ticketRepository.findTicketsByCustomerId(12);
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link Ticket} as the base ticket entity</li>
                <li>{@link Seat} for seat number resolution</li>
                <li>{@link Expedition} for expedition metadata</li>
                <li>{@link City} for departure and arrival city names</li>
                <li>{@link TicketRepoDTO} as a projection DTO</li>
                <li>{@link Query} for JPQL constructor expression</li>
            </ul>

        </p>

        @param customerId the identifier of the customer whose tickets will be retrieved

        @return a list of {@link TicketRepoDTO} containing all tickets owned by the customer;
        returns an empty list if the customer has no tickets
    */
    @Query("""
        SELECT new com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories.TicketRepoDTO(
            t.PNR,
            s.seatNo,
            e.id,
            e.companyId,
            dc.name,
            ac.name,
            e.dateAndTime,
            e.duration
        )
        FROM Ticket t
            JOIN Seat s ON t.seatId = s.id
            JOIN Expedition e ON s.expeditionId = e.id
            JOIN City dc ON e.departureCityId = dc.id
            JOIN City ac ON e.arrivalCityId = ac.id
        WHERE t.customerId = :customerId
        """
    )
    List<TicketRepoDTO> findTicketsByCustomerId(int customerId);
}
