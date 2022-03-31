package com.carrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Car {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private int availableSeats;

    @Column
    private String transmission;

    @Column
    private double dayPrice;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return availableSeats == car.availableSeats && Double.compare(car.dayPrice, dayPrice) == 0 && Objects.equals(id, car.id) && Objects.equals(transmission, car.transmission) && Objects.equals(user, car.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, availableSeats, transmission, dayPrice, user);
    }
}
