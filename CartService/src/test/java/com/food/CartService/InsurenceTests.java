package com.food.CartService;

import com.food.CartService.entity.Appointment;
import com.food.CartService.entity.Insurence;
import com.food.CartService.entity.Patient;
import com.food.CartService.service.AppoitmentService;
import com.food.CartService.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class InsurenceTests {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private AppoitmentService appointmentservice;

    @Test
    public void testInsurence(){
        Insurence insurence = Insurence.builder()
                .policyNumber("HDFC_1234")
                .provider("HDFC")
                .validUntil(LocalDate.of(2030,12,10))
                .build();

        Patient patient = insuranceService.assignInsuranceToPatient(insurence,2L);
        System.out.println(patient);
    }


    @Test
    public void testCreateAppointment(){
        Appointment appointment1 = Appointment.builder()
                .appoitmentTime(LocalDateTime.of(2025, 1,14,0,0))
                .reason("Checkup")
                .build();

        var newAppointment = appointmentservice.createNewAppointment(appointment1, 1L, 2L);
       System.out.println(newAppointment);

        var updateAppointment = appointmentservice.reAssignAppointmentToAnotherDoctor(newAppointment.getId(),3L);
         System.out.println(updateAppointment);

    }


}
