package com.carrental.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

}
