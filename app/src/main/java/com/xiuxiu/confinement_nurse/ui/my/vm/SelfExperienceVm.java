package com.xiuxiu.confinement_nurse.ui.my.vm;

import java.io.Serializable;

public class SelfExperienceVm implements Serializable {
    private SelfExperience item = new SelfExperience();

    public SelfExperience getItem() {
        return item == null ? new SelfExperience() : item;
    }

    public void setItem(SelfExperience item) {
        this.item = item;
    }

    public static class SelfExperience implements Serializable {
        private String content;
        private String video;
        private String id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }
}
