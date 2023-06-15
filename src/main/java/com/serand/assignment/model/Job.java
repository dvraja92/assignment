package com.serand.assignment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "jobs")
@Data
@Builder(toBuilder = true)
public class Job {
    private String id;
    private String name;
    private String description;
    @DocumentReference
    private Company company;

    @JsonCreator
    public Job(@JsonProperty("id") String id, @JsonProperty("name") String name,
               @JsonProperty("description") String description, @JsonProperty("company") Company company) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.company = company;
    }
}
