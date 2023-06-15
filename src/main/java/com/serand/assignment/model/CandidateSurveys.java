package com.serand.assignment.model;

import com.serand.assignment.enums.SurveyStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import static com.serand.assignment.enums.SurveyStatus.COMPLETED;

@Document(collection = "candidateSurveys")
@Data
@Builder(toBuilder = true)
public class CandidateSurveys {
    @Id
    private String id;
    @DocumentReference
    private Candidate candidate;
    @DocumentReference
    private Survey survey;

    private SurveyStatus status = COMPLETED;
    private int score;
}
