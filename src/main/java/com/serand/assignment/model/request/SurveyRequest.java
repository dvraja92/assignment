package com.serand.assignment.model.request;

import com.serand.assignment.model.Survey.SurveyQuestions;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;

@Getter
public class SurveyRequest {

  @NotBlank(message = "Company Id cannot be blank.")
  private String companyId;
  @NotBlank(message = "Job Id cannot be blank.")
  private String jobId;
  @NotBlank(message = "Name cannot be blank.")
  private String name;
  private List<SurveyQuestions> surveyQuestions;
}
