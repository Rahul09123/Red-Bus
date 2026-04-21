package com.shubilet.expedition_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shubilet.expedition_service.models.City;


@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    /***

        Operation: ExistsByName

        Checks whether a city with the given name exists in the system.
        This method is typically used during expedition creation or validation
        flows to ensure that referenced departure or arrival cities are already
        registered in the database.

        The query evaluates the existence of at least one {@link City} entity
        matching the provided name and returns a boolean result without loading
        the full entity.

        <p>

            Usage:

            <pre>
                boolean cityExists =
                    cityRepository.existsByName("Ankara");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link City} as the underlying JPA entity</li>
                <li>{@link Query} for custom JPQL existence check</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
            </ul>

        </p>

        @param name the name of the city to check for existence

        @return {@code true} if a city with the given name exists,
        otherwise {@code false}
    */
    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
        FROM City c
        WHERE c.name = :name
    """)
    boolean existsByName(@Param("name") String name);

    /***

        Operation: FindIdByName

        Retrieves the unique identifier of a city based on its name.
        This method executes a JPQL query that attempts to resolve the {@link City}
        entity identifier for the given city name and returns a default value of
        {@code -1} when no matching city is found. This approach avoids returning
        {@code null} and simplifies downstream validation and control flow.

        <p>

            Usage:

            <pre>
                int cityId =
                    cityRepository.findIdByName("Ankara");
            </pre>

        </p>

        <p>

            Uses:

            <ul>
                <li>{@link City} as the underlying JPA entity</li>
                <li>{@link Query} for custom JPQL identifier resolution</li>
                <li>{@link Param} for named parameter binding</li>
                <li>{@link JpaRepository} for repository abstraction</li>
                <li>{@code COALESCE} to provide a safe default value when no result is found</li>
            </ul>

        </p>

        @param name the name of the city whose identifier is to be resolved

        @return the city identifier if found, or {@code -1} if no city exists with the given name
    */
    @Query("""
        SELECT COALESCE(
                (
                    SELECT c.id 
                    FROM City c 
                    WHERE c.name = :name
                ),
                -1
            )
    """)
    int findIdByName(@Param("name") String name);
}
