package com.food.CartService.service;

import com.food.CartService.entity.Insurence;
import com.food.CartService.entity.Patient;
import com.food.CartService.repo.InsurenceRepository;
import com.food.CartService.repo.PatientRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsurenceRepository insurenceRepository;
    private final PatientRepo patientRepo;

    @Transactional
    public Patient assignInsuranceToPatient(Insurence insurence, Long patientId){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient Not fount"));

        patient.setInsurance(insurence);

        return patient;
    }
}
