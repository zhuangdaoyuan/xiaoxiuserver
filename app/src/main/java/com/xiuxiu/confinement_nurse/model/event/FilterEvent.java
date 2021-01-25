package com.xiuxiu.confinement_nurse.model.event;

import com.jeremyliao.eventbus.base.annotation.SmartEvent;

import java.io.Serializable;
@SmartEvent(keys = {"FilterChange"})
public class FilterEvent implements Serializable {
}
