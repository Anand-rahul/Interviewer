package com.example.Interviewer.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "GPTLogs")
public class GPTModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    @Column(name = "question")
    private String Question;
    @Column(name = "prompt" ,length = 8000)
    private String Prompt;
    public GPTModel(Long id, String question, String prompt) {
        Id = id;
        Question = question;
        Prompt = prompt;
    }
    public GPTModel(){}
    public Long getId() {
        return Id;
    }
    public void setId(Long id) {
        Id = id;
    }
    public String getQuestion() {
        return Question;
    }
    public void setQuestion(String question) {
        Question = question;
    }
    public String getPrompt() {
        return Prompt;
    }
    public void setPrompt(String prompt) {
        Prompt = prompt;
    }
    @Override
    public String toString() {
        return "GPTModel{" +
                "Id=" + Id +
                ", Question='" + Question + '\'' +
                ", Prompt='" + Prompt + '\'' +
                '}';
    }
}
