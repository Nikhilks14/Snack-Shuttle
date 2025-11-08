package com.food.CartService.service;

import com.food.CartService.entity.Appointment;
import com.food.CartService.entity.Doctor;
import com.food.CartService.entity.Patient;
import com.food.CartService.repo.AppointmentRepository;
import com.food.CartService.repo.DoctorRepository;
import com.food.CartService.repo.PatientRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppoitmentService {

    private final AppointmentRepository appointment;
    private final DoctorRepository doctorRepository;
    private final PatientRepo patientRepo;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createNewAppointment(Appointment appointment, Long doctorId, Long patientId){
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepo.findById(patientId).orElseThrow();

        if(appointment.getId() != null) throw  new IllegalArgumentException("Appointment should not ");

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        patient.getAppointment().add(appointment);

        return  appointmentRepository.save(appointment);

    }

    @Transactional
    public Appointment reAssignAppointmentToAnotherDoctor(Long appointment, Long doctorId){

        Appointment appointment1 = appointmentRepository.findById(appointment).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        appointment1.setDoctor(doctor);
        doctor.getAppointmentList().add(appointment1);

        return appointment1;
    }
}
