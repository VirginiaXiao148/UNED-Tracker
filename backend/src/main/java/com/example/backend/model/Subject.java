package com.example.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.example.backend.model.SubjectType;
import com.example.backend.model.SubjectStatus;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int credits;
    @Enumerated(EnumType.STRING)
    private SubjectType type; // Basic, Mandatory or Optional
    private int year; // 1, 2, 3 or 4
    private BigDecimal note;
    @Enumerated(EnumType.STRING)
    private SubjectStatus status; // Passed, Failed or Matriculated

}