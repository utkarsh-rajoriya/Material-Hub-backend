package com.utkarsh.MaterialHub.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MakeAbstractRequest {

    @Value("${ABSTRACT_API_KEY}")
    private String API_KEY;
    private final String BASE_URL = "https://emailvalidation.abstractapi.com/v1/";

    public boolean isValidEmail(String email) {
        try {
            String url = BASE_URL + "?api_key=" + API_KEY + "&email=" + email;

            Content content = Request.Get(url)
                    .connectTimeout(5000)
                    .socketTimeout(5000)
                    .execute()
                    .returnContent();

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content.asString());

            String deliverability = root.path("deliverability").asText();
            return "DELIVERABLE".equalsIgnoreCase(deliverability);

        } catch (Exception e) {
            System.out.println("Error while calling API: " + e.getMessage());
            return false; // fallback: consider invalid if error occurs
        }
    }
}
