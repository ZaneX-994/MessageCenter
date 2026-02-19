package com.bytewizard.msgmate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgQueueTimerModel extends BaseModel implements Serializable {
    private Long id;

    private String msgId;

    private String req;

    private Long sendTimestamp;

    private int status;
}
