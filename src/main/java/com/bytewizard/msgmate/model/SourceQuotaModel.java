package com.bytewizard.msgmate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SourceQuotaModel implements Serializable {
    private Long id;

    private int num;

    private int unit;

    private int channel;

    private String sourceId;
}
