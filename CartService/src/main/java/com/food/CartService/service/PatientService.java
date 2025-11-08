package com.food.CartService.service;

import com.food.CartService.entity.Patient;
import com.food.CartService.repo.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepo patientRepo;

    public Patient getPatientById(Long id) {

        Patient p1 = patientRepo.findById(id).orElseThrow();
        Patient p2 = patientRepo.findById(id).orElseThrow();
        System.out.println(p1 == p2);
        p1.setName("Yoyo");
//        patientRepository.save(p1);
        return p1;
    }
}
