package com.serand.assignment.repository;

import com.serand.assignment.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

    List<Survey> findByCompanyId(String companyId);

    Optional<Survey> findByName(String name);

    List<Survey> findByJobId(String jobId);
}