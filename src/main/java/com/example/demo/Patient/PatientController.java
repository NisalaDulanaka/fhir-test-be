package com.example.demo.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.example.demo.utils.JsonFactory;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/patient", produces = "application/json")
public class PatientController {

    PatientRepository patientRepository;
    JsonFactory jsonFactory;
    FhirContext ctx;
    IGenericClient client;
    String serverR4;

    {
        serverR4=System.getenv("fhir_r4_url");

        ctx = FhirContext.forR4(); // Initialize context
        client = ctx.newRestfulGenericClient(serverR4); // Create the client
    }

    @Autowired
    public PatientController(PatientRepository patientRepository, JsonFactory jsonFactory) {
        this.patientRepository = patientRepository
                .setContext(ctx)
                .setClient(client);
        this.jsonFactory = jsonFactory.setContext(ctx);
    }

    @GetMapping
    public String getPatients() {

        // Perform a search
        var patients = patientRepository.getPatients();
        return jsonFactory
                .lListResource()
                .convertToString(patients);
    }

    @GetMapping(path = "/{id}")
    public String getPatient(@PathVariable String id) {

        var patient = patientRepository.getPatient(id);
        return jsonFactory
                .singleResource()
                .convertToString(patient);
    }

    @GetMapping(path = "/search")
    public Object searchPatient(@RequestParam String q) {
        q = q.isBlank() ? "" : q;

        var patients = patientRepository.searchPatients(q);
        return patients.stream().map( patient -> ctx.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeToString(patient));
    }
}
