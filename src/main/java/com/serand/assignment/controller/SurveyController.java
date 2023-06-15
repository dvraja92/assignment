package com.serand.assignment.controller;


import com.serand.assignment.model.CandidateSurveys;
import com.serand.assignment.model.Survey;
import com.serand.assignment.model.request.CandidateSurveyRequest;
import com.serand.assignment.model.request.SurveyRequest;
import com.serand.assignment.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Responsible for all user related endpoints.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/surveys")
public class SurveyController {
	@Autowired
	private SurveyService surveyService;

	@GetMapping("/company/{companyId}")
	public List<Survey> getSurveysByCompanyId(@PathVariable("companyId") String companyId) {
		return surveyService.findByCompany(companyId);
	}

	@PostMapping("/company")
	public ResponseEntity<Survey> createSurvey(@RequestBody SurveyRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.createSurvey(request));
	}

	@DeleteMapping("/company/{id}")
	public void delete(@PathVariable("id") String id) {
		surveyService.delete(id);
	}

	@PostMapping("/company/{candidateId}/{surveyId}")
	public CandidateSurveys addCandidateSurvey(@PathVariable("candidateId") String candidateId,
											   @PathVariable("surveyId") String surveyId,
											   @RequestBody CandidateSurveyRequest candidateSurveyRequest) {
		return surveyService.addCandidateSurvey(candidateId, surveyId, candidateSurveyRequest);
	}

	@GetMapping("/search")
	public List<CandidateSurveys> search(@RequestParam(value = "jobId", required = false) String jobId,
			@RequestParam(value = "score", required = false) Integer score) {
		return surveyService.searchCandidateSubmission(jobId, score);
	}

}