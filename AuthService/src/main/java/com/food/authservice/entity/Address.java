package com.food.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity

public class Address {

    @Id
    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String tag;
    private Double latitude;  // for precise delivery (optional)
    private Double longitude; // for precise delivery (optional)

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
