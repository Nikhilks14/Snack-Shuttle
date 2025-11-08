package com.food.CartService;

import com.food.CartService.entity.Patient;
import com.food.CartService.repo.PatientRepo;
import com.food.CartService.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class PatientTests {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepo(){
        List<Patient> allPatient = patientRepo.findAll();
        System.out.println(allPatient);
    }

    @Test
    public void testTransactionMethods() {
        Patient patient = patientService.getPatientById(11L);

        System.out.println(patient);


        Page<Patient> patientPage = patientRepo.findAllPatients(PageRequest.of(0,4, Sort.by("name")));
        for (Patient patient1: patientPage){
            System.out.println(patient1);
        }
    }
}
