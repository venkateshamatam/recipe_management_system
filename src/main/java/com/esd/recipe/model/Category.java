package com.esd.recipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;

@Entity
@Table(name = "categories")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) // This will prevent Jackson from serializing null fields.
@JsonIgnoreProperties(ignoreUnknown = true) // This will prevent Jackson from failing when it encounters an unknown property.
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;


    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories")
    private Collection<Recipe> recipes;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

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

}
