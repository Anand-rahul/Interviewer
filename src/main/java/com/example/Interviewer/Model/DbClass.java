package com.example.Interviewer.Model;

import jakarta.persistence.*;

@Entity
@Table(name="\"InterviewQuestion\"")
public class DbClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "Question")
    private String Question;
    public DbClass(Long id, String Question) {
        id = id;
        Question = Question;
    }
    public DbClass(){}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        id = id;
    }
    public String getQuestion() {
        return Question;
    }
    public void setQuestion(String Question) {
        Question = Question;
    }
    @Override
    public String toString() {
        return "DbClass{" +
                "Id=" + id +
                ", Question='" + Question + '\'' +
                '}';
    }
}
