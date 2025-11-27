package com.example.backend.controller;

import com.example.backend.model.Subject;
import com.example.backend.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin so Next.js can access the API
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Get all subjects
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    // Get stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMatriculationStatistics() {
        Map<String, Object> stats = subjectService.getMatriculationStatistics();
        return ResponseEntity.ok(stats);
    }

    // Post a new subject
    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        Subject createdSubject = subjectService.createSubject(subject);
        return ResponseEntity.ok(createdSubject);
    }

    // Update a subject
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject subject) {
        Subject updatedSubject = subjectService.updateSubject(id, subject);
        return ResponseEntity.ok(updatedSubject);
    }

    // Delete a subject
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

}
