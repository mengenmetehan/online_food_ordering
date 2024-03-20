package com.mengen.repository;

import com.mengen.exceptions.RestaurantNotFoundException;
import com.mengen.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


    Restaurant findByOwnerId(Long userId) throws RestaurantNotFoundException;

    Restaurant findByOwnerEmail(String email) throws RestaurantNotFoundException;

    @Query("SELECT r FROM Restaurant r WHERE lower(r.name) LIKE lower(concat('%', :query, '%'))" +
            "OR lower(r.cuisineType) LIKE lower(concat('%', :query, '%'))")
    List<Restaurant> findBySearchQuery(String query);

}
