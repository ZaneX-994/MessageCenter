package com.bytewizard.msgmate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgRecordModel extends BaseModel implements Serializable {
    private Long id;

    private String msgId;

    private String sourceId;

    private int channel;

    private String subject;

    private String to;

    private String templateId;

    private String templateData;

    private int status;

    private int retryCount; //重试次数，默认为 0
}
