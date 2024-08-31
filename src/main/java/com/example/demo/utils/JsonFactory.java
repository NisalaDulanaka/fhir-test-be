package com.example.demo.utils;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JsonFactory {

    private FhirContext context;
    private boolean singleResource = false;
    private boolean listResource = false;

    public String convertToString(Object resource) {
        String value = null;

        if(singleResource) value = convertSingleResource(resource);

        if(listResource) {
            List<Object> list =
            (resource instanceof List)? (List<Object>) resource : new ArrayList<>();

            value = convertListResource(list);
        }

        return value;
    }

    private String convertListResource(List<Object> resourceList) {
        StringBuilder builder = new StringBuilder()
                .append("[");
        resourceList.forEach(resource -> {
            StringBuilder resourceString = new StringBuilder();

            if( resource instanceof IBase ) {
                resourceString.append(context.newJsonParser()
                        .setPrettyPrint(true)
                        .encodeToString((IBase) resource));
            }
            else resourceString.append(resource.toString());

            if( resourceString.charAt(0) != '{' ) {
                resourceString.insert(0,'{');
            }

            if( resourceString.charAt(resourceString.length() - 1) != '}' ) {
                resourceString.append('}');
            }

            builder.append(resourceString).append(",");
        });
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }

    private String convertSingleResource(Object resource) {
        StringBuilder builder = new StringBuilder();

        if( resource instanceof IBase ) {
            builder.append(context.newJsonParser()
                    .setPrettyPrint(true)
                    .encodeToString((IBase) resource));
        }
        else builder.append(resource.toString());

        if( builder.charAt(0) != '{' ) {
            builder.insert(0,'{');
        }

        if( builder.charAt(builder.length() - 1) != '}' ) {
            builder.append('}');
        }

        return builder.toString();
    }

    public JsonFactory singleResource() {
        this.singleResource = true;
        return this;
    }

    public JsonFactory lListResource() {
        this.listResource = true;
        return this;
    }

    public JsonFactory setContext(FhirContext context) {
        this.context = context;
        return this;
    }
}
