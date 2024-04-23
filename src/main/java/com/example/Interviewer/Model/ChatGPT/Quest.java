package com.example.Interviewer.Model.ChatGPT;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Quest {
    private String ques;

    public Quest(String ques) {
        this.ques = ques;
    }
    public Quest() {
    }
}
