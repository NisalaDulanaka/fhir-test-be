package com.example.demo.Patient;

import org.hl7.fhir.r4.model.Patient;

import java.util.List;

public interface IPatientRepository {

    Patient getPatient(String id);

    List<Patient> getPatients();

    List<Patient> searchPatients(String query);

    boolean addPatient(Patient patient);

    boolean updatePatient(Patient patient);

    boolean deletePatient(String id);
}
