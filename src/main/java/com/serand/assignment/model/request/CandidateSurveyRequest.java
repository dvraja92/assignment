package com.serand.assignment.model.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class CandidateSurveyRequest {
    private Map<String, String> answers;
}
