package com.serand.assignment.service;

import com.serand.assignment.model.Candidate;
import com.serand.assignment.model.Job;
import com.serand.assignment.model.request.CandidateRequest;
import com.serand.assignment.repository.CandidateRepository;
import com.serand.assignment.repository.JobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;

    public Candidate save(CandidateRequest candidate) {
        final List<Job> jobs = jobRepository.findAllById(candidate.getJobId());
        if (jobs.isEmpty()) {
            throw new RuntimeException("Applied jobs cannot be blank.");
        }
        return candidateRepository.save(Candidate.builder()
                .name(candidate.getName())
                .appliedJobs(jobs).build());
    }
}
