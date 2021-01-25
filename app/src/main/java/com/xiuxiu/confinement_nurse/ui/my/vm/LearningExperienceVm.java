package com.xiuxiu.confinement_nurse.ui.my.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LearningExperienceVm implements Serializable {

    private List<LearningExperience> items;

    public List<LearningExperience> getItems() {
        return items == null ? new ArrayList<>() : items;
    }

    public void setItems(List<LearningExperience> items) {
        this.items = items;
    }

    public static class LearningExperience implements Serializable {
        private String id;
        private String type;
        private String image;
        private String startTime;
        private String endTime;
        private String school;
        private String subject;
        private String level;
        private String degree;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }
    }
}
