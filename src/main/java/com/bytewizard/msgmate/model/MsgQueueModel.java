package com.bytewizard.msgmate.model;

import lombok.Data;

import java.io.Serializable;

/**
 * TemplateModel 消息模板
 *
 **/
@Data
public class MsgQueueModel extends BaseModel implements Serializable {

    private Long id;

    private String msgId;

    private String to;

    private String subject;

    private int priority;

    private int channel;

    private String templateId;

    private String templateData;

    private int status;


    @Override
    public String toString() {
        return "MsgQueueModel{" +
                "id=" + id +
                ", msgId='" + msgId + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", priority=" + priority +
                ", channel=" + channel +
                ", templateId='" + templateId + '\'' +
                ", templateData='" + templateData + '\'' +
                ", status=" + status +
                '}';
    }
}

