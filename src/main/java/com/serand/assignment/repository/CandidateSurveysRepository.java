package com.serand.assignment.repository;

import com.serand.assignment.enums.SurveyStatus;
import com.serand.assignment.model.CandidateSurveys;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateSurveysRepository extends MongoRepository<CandidateSurveys, String> {

    Optional<CandidateSurveys> findByCandidateIdAndSurveyId(String candidateId, String surveyId);

    List<CandidateSurveys> findBySurveyIdAndScore(List<String> id, Integer score);
    List<CandidateSurveys> findByScore(Integer score);

    List<CandidateSurveys> findBySurveyId(List<String> ids);
}