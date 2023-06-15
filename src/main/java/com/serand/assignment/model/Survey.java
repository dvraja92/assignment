package com.serand.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "surveys")
@Data
@Builder(toBuilder = true)
public class Survey {
    @Id
    private String id;
    private String name;
    @DocumentReference
    private Company company;
    @DocumentReference
    private Job job;
    private List<SurveyQuestions> questions;

    public Survey(@JsonProperty("id") String id, @JsonProperty("name") String name,
                  @JsonProperty("company") Company company, @JsonProperty("job") Job job,
                  @JsonProperty("surveyQuestions") List<SurveyQuestions> questions) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.job = job;
        this.questions = questions;
    }

    @Data
    @Builder(toBuilder = true)
    public static class SurveyQuestions {
        @NotBlank(message = "Cannot be blank.")
        private String question;
        private List<String> expectedAnswer;
        private int marks; //assuming equal marks for each answer
        private int defaultMarks; //assuming equal marks for each answer

        public SurveyQuestions(
                @JsonProperty("question") String question,
                @JsonProperty("expectedAnswer") List<String> expectedAnswer,
                @JsonProperty("marks") int marks,
                @JsonProperty("defaultMarks") int defaultMarks) {
            this.question = question;
            this.expectedAnswer = expectedAnswer;
            this.marks = marks;
            this.defaultMarks = defaultMarks;
        }
    }
}
