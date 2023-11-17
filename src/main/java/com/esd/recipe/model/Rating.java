package com.esd.recipe.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL) // This will prevent Jackson from serializing null fields.
@JsonIgnoreProperties(ignoreUnknown = true) // This will prevent Jackson from failing when it encounters an unknown property.
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // This will prevent Jackson from infinite-looping when it gets to the User class.
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = true)
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true) // This will serialize the user reference as the user's ID.
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonBackReference // This will prevent Jackson from infinite-looping when it gets to the Recipe class.
    private Recipe recipe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", value=" + value +
                ", user=" + user +
                ", recipe=" + recipe +
                '}';
    }
}
