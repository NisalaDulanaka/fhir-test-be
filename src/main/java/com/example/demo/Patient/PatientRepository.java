package com.example.demo.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientRepository implements IPatientRepository{

    private FhirContext ctx;
    private IGenericClient client;
    private List<Patient> patientList = new ArrayList<>();

    @Override
    public Patient getPatient(String id) {
        // Perform a search
        return client.read().resource(Patient.class)
                .withId(id).encodedJson().execute();
    }

    @Override
    public List<Patient> getPatients() {
        if(!this.patientList.isEmpty()) return this.patientList;

        Bundle bundle = client.search().forResource(Patient.class)
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();

        this.patientList = bundle.getEntry()
                .stream()
                .map((bundleEntryComponent)
                        -> (Patient) bundleEntryComponent.getResource())
                .toList();

        return this.patientList;
    }

    @Override
    public List<Patient> searchPatients(String query) {
        // Perform a search
        Bundle results = client.search()
                .forResource(Patient.class)
                .where(Patient.NAME.contains().value(query))
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();

        return results.getEntry()
                .stream()
                .map((bundleEntryComponent)
                        -> (Patient) bundleEntryComponent.getResource())
                .toList();
    }

    @Override
    public boolean addPatient(Patient patient) {
        return false;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        return false;
    }

    @Override
    public boolean deletePatient(String id) {
        return false;
    }

    public PatientRepository setContext(FhirContext ctx) {
        this.ctx = ctx;
        return this;
    }

    public PatientRepository setClient(IGenericClient client) {
        this.client = client;
        return this;
    }
}
