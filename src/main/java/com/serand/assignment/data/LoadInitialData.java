package com.serand.assignment.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serand.assignment.model.Company;
import com.serand.assignment.repository.CompanyRepository;
import com.serand.assignment.repository.JobRepository;
import com.serand.assignment.repository.SurveyRepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoadInitialData {

  private final JobRepository jobRepository;
  private final SurveyRepository surveyRepository;
  private final CompanyRepository companyRepository;

  @Value("${assignment.loadInitialData}")
  private boolean loadInitialData;

  @PostConstruct
  public void addInitialData() {
    ObjectMapper objectMapper = new ObjectMapper();
    if (!loadInitialData) {
      return;
    }
    try {
      companyRepository.saveAll(objectMapper.readValue(
          this.getClass().getClassLoader().getResourceAsStream("data/company.json"),
          new TypeReference<>() {
          }));
      jobRepository.saveAll(objectMapper.readValue(
          this.getClass().getClassLoader().getResourceAsStream("data/jobs.json"),
          new TypeReference<>() {
          }));

      surveyRepository.saveAll(objectMapper.readValue(
          this.getClass().getClassLoader().getResourceAsStream("data/surveys.json"),
          new TypeReference<>() {
          }));

    } catch (IOException e) {
      log.error("Error in adding initial data.");
    }
  }
}
