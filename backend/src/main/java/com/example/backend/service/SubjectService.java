package com.example.backend.service;

import com.example.backend.model.Subject;
import com.example.backend.model.SubjectStatus;
import com.example.backend.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // Crud operations
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
    
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject updateSubject(Long id, Subject updatedSubject) {
        return subjectRepository.findById(id).map(subject -> {
            subject.setName(updatedSubject.getName());
            subject.setCredits(updatedSubject.getCredits());
            subject.setType(updatedSubject.getType());
            subject.setYear(updatedSubject.getYear());
            subject.setNote(updatedSubject.getNote());
            subject.setStatus(updatedSubject.getStatus());
            return subjectRepository.save(subject);
        }).orElse(null);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    // Matriculation statistics
    public Map<String, Object> getMatriculationStatistics() {
        List<Subject> subjects = subjectRepository.findAll();

        int totalSubjects = subjects.size();
        int passedSubjects = (int) subjects.stream()
                .filter(subject -> subject.getStatus() == SubjectStatus.PASSED)
                .count();
        
        int creditsObtained = subjects.stream()
                .filter(subject -> subject.getStatus() == SubjectStatus.PASSED)
                .mapToInt(Subject::getCredits)
                .sum();
        int totalCredits = 240; // Assuming total credits required for graduation is 240
        double progressPercentage = (double) creditsObtained / totalCredits * 100;

        // Calculate pondered average note
        BigDecimal totalWeightedNotes = subjects.stream()
                .filter(subject -> subject.getStatus() == SubjectStatus.PASSED)
                .filter(subject -> subject.getNote() != null)
                .map(subject -> subject.getNote().multiply(new BigDecimal(subject.getCredits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        

        BigDecimal averageNote = BigDecimal.ZERO;
        if (creditsObtained > 0) {
            averageNote = totalWeightedNotes.divide(new BigDecimal(creditsObtained), 2, RoundingMode.HALF_UP);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("averageNote", averageNote);
        stats.put("creditsObtained", creditsObtained);
        stats.put("progressPercentage", Math.round(progressPercentage * 100.0) / 100.0);
        stats.put("totalSubjects", totalSubjects);

        return stats;
    }

}
