package com.example.Interviewer.Model.ChatGPT;

import java.io.Serializable;
import java.util.List;

public class ChatResponse implements Serializable {
    private List<Choice> choices;
    public ChatResponse(){
    }
    public ChatResponse(List<Choice> choices) {
        this.choices = choices;
    }
    public List<Choice> getChoices() {
        return choices;
    }
    @Override
    public String toString() {
        return "ChatResponse{" +
                "choices=" + choices +
                '}';
    }
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
    public static class Choice {
        private int index;
        private Message message;
        public Choice(int index, Message message) {
            this.index = index;
            this.message = message;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public Message getMessage() {
            return message;
        }
        public void setMessage(Message message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Choice{" +
                    "index=" + index +
                    ", message=" + message +
                    '}';
        }
    }
}