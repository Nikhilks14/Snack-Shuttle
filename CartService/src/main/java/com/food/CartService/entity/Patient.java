package com.food.CartService.entity;


import com.food.CartService.entity.type.BloodGroup;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@Table(
        name = "patient",
        uniqueConstraints = {
               // @UniqueConstraint(name = "unique_patient_email", columnNames = {"email"}),
                @UniqueConstraint(name = "unique_patient_name_birthdate", columnNames = {"name","birthDate"})
        },
        indexes = {
                @Index(name = "idx_patient_birth_date" , columnList = "birthDate")
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false )
    private String name;
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    @CreationTimestamp
    @Column(updatable = false)
    private String creationAt;

    private String gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @OneToOne(cascade = {CascadeType.MERGE , CascadeType.PERSIST}) // MERGE for update and
    @JoinColumn(name = "patient_insurance_id")
    private Insurence insurance;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE} , orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment> appointment = new ArrayList<>();
}
