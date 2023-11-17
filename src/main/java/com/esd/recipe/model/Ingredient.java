package com.esd.recipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL) // This will prevent Jackson from serializing null fields.
@JsonIgnoreProperties(ignoreUnknown = true) // This will prevent Jackson from failing when it encounters an unknown property.
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public Ingredient() {

    }

    public Ingredient(String name) {
        super();
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
