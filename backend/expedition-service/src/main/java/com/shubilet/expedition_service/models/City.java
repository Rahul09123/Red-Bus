package com.shubilet.expedition_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Represents a city entity that can be used in expedition routes.
 * Example: İzmir, Ankara, İstanbul, etc.
 */
@Entity
@Table(name = "cities")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    // ------------------------
    // Primary Key
    // ------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ------------------------
    // Fields
    // ------------------------
    @NotBlank
    @Column(nullable = false, updatable = false, unique = true)
    private String name;


    // ------------------------
    // Constructors
    // ------------------------
    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    // ------------------------
    // Getters and Setters
    // ------------------------
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // ------------------------
    // Equality & HashCode
    // ------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return id == city.id && name.equals(city.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name);
    }

    // ------------------------
    // String Representation
    // ------------------------
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
