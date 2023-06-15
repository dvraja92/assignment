package com.serand.assignment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CandidateRequest {
    @NotBlank(message = "Name cannot be blank.")
    private String name;
    @NotNull(message = "Jobs cannot be empty.")
    private List<String> jobId;
}
