package com.xiuxiu.confinement_nurse.model.event;

import com.jeremyliao.eventbus.base.annotation.SmartEventConfig;

import java.io.Serializable;

@SmartEventConfig(packageName = "com.xiuxiu.confinement_nurse", busName = "EventBus")
public class EventBusConfig implements Serializable {
}
