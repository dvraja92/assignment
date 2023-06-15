package com.serand.assignment.service;

import static com.serand.assignment.enums.SurveyStatus.COMPLETED;

import com.serand.assignment.model.Candidate;
import com.serand.assignment.model.CandidateSurveys;
import com.serand.assignment.model.Job;
import com.serand.assignment.model.Survey;
import com.serand.assignment.model.Survey.SurveyQuestions;
import com.serand.assignment.model.request.CandidateSurveyRequest;
import com.serand.assignment.model.request.SurveyRequest;
import com.serand.assignment.repository.CandidateRepository;
import com.serand.assignment.repository.CandidateSurveysRepository;
import com.serand.assignment.repository.JobRepository;
import com.serand.assignment.repository.SurveyRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

  private final SurveyRepository surveyRepository;
  private final JobRepository jobRepository;
  private final CandidateRepository candidateRepository;
  private final CandidateSurveysRepository candidateSurveysRepository;

  public List<Survey> findByCompany(String companyId) {
    return surveyRepository.findByCompanyId(companyId);
  }


  public Survey createSurvey(SurveyRequest request) {
    AtomicReference<Job> job = new AtomicReference<>();
    jobRepository.findById(request.getJobId()).ifPresent(job1 -> {
      if (!job1.getCompany().getId().equals(request.getCompanyId())) {
        throw new RuntimeException("Company not associated with job.");
      }
      job.set(job1);
    });

    surveyRepository.findByName(request.getName()).ifPresent(survey -> {
      throw new RuntimeException(
          String.format("Survey with name %s already exists.", survey.getName()));
    });

    return surveyRepository.save(Survey.builder().questions(request.getSurveyQuestions())
        .name(request.getName())
        .company(job.get().getCompany())
        .job(job.get())
        .build());
  }

  public void delete(String id) {
    surveyRepository.findById(id).ifPresentOrElse(survey -> {
      surveyRepository.deleteById(id);
    }, () -> {
      throw new RuntimeException("Survey not found for given id");
    });
  }

  public CandidateSurveys addCandidateSurvey(String candidateId, String surveyId,
      CandidateSurveyRequest candidateSurveyRequest) {
    AtomicReference<Survey> survey = new AtomicReference<>();
    AtomicReference<Candidate> candidate = new AtomicReference<>();
    surveyRepository.findById(surveyId).ifPresentOrElse(survey::set,
        () -> {
          throw new RuntimeException("Survey does not exists.");
        });
    candidateRepository.findById(candidateId).ifPresentOrElse(candidate::set,
        () -> {
          throw new RuntimeException("Candidate does not exists.");
        });

    candidateSurveysRepository.findByCandidateIdAndSurveyId(candidateId, surveyId)
        .ifPresent(candidateSurveys -> {
          throw new RuntimeException("Candidate submission already exists.");
        });

    int scores = prepareCandidateScores(candidateSurveyRequest.getAnswers(),
        survey.get().getQuestions());

    return candidateSurveysRepository.save(CandidateSurveys.builder()
        .survey(survey.get())
        .status(COMPLETED)
        .candidate(candidate.get())
        .score(scores)
        .build());
  }

  private int prepareCandidateScores(Map<String, String> answers,
      List<SurveyQuestions> surveyQuestions) {
    return answers.entrySet().stream().map(entry -> {
      final List<String> candidateAnswer = List.of(entry.getValue().split(","));
      AtomicInteger score = new AtomicInteger();
      surveyQuestions.stream()
          .filter(surveyQuestions1 -> surveyQuestions1
              .getQuestion().equalsIgnoreCase(entry.getKey()))
          .findFirst().ifPresent(quest -> calculateScores(candidateAnswer, score, quest));
      return score;
    }).mapToInt(AtomicInteger::get).sum();

  }

  private void calculateScores(List<String> candidateAnswer, AtomicInteger score,
      SurveyQuestions quest) {
    final long correctAnswer = candidateAnswer.stream()
        .filter(ans -> quest.getExpectedAnswer().contains(ans.toLowerCase().trim())).count();
    score.addAndGet(Math.toIntExact(correctAnswer * quest.getMarks()));
    if (correctAnswer != quest.getExpectedAnswer().size()) {
      score.addAndGet(quest.getDefaultMarks());
    }
  }

  public List<CandidateSurveys> searchCandidateSubmission(String jobId, Integer score) {
    if (StringUtils.hasText(jobId) && Objects.nonNull(score)) {
      return candidateSurveysRepository.findBySurveyIdAndScore(
          surveyRepository.findByJobId(jobId).stream().map(Survey::getId).collect(
              Collectors.toList()), score);
    } else if (StringUtils.hasText(jobId)) {
      return candidateSurveysRepository.findBySurveyId(
          surveyRepository.findByJobId(jobId).stream().map(Survey::getId).collect(
              Collectors.toList()));
    } else if (Objects.nonNull(score)) {
      return candidateSurveysRepository.findByScore(score);
    } else {
      return candidateSurveysRepository.findAll();
    }
  }
}
