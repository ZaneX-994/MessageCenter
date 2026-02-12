package com.bytewizard.msgmate.model;

import lombok.Data;

import java.io.Serializable;

/**
 * TemplateModel 消息模板
 *
 **/
@Data
public class TemplateModel extends BaseModel implements Serializable {

    private Long id;

    private String templateId;

    private String relTemplateId;

    private String name;

    private String signName;

    private String sourceId;

    private int channel;

    private String subject;

    private String content;

    private int status;

    @Override
    public String toString() {
        return "TemplateModel{" +
                "id=" + id +
                ", templateId='" + templateId + '\'' +
                ", relTemplateId='" + relTemplateId + '\'' +
                ", name='" + name + '\'' +
                ", signName='" + signName + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", channel=" + channel +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
