package com.xiuxiu.confinement_nurse.model.event;

import com.jeremyliao.eventbus.base.annotation.SmartEvent;

import java.io.Serializable;

@SmartEvent(keys = {"LearningExperience","educationalExperience", "OtherExperience", "SelfEvaluation"})
public class ExperienceEvent implements Serializable {
    public static final int KEY_DELETE = 1;
    private int type;

    public ExperienceEvent() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ExperienceEvent(int type) {
        this.type = type;
    }
}
