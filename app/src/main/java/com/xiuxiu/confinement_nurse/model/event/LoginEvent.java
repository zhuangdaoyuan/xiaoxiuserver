package com.xiuxiu.confinement_nurse.model.event;

import com.google.gson.annotations.SerializedName;
import com.jeremyliao.eventbus.base.annotation.SmartEvent;

import java.io.Serializable;

@SmartEvent(keys = {"LoginEvent", "LogoutEvent"})
public class LoginEvent implements Serializable {
}
