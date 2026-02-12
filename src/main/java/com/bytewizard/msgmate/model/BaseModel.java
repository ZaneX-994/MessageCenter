package com.bytewizard.msgmate.model;

import lombok.Data;

import java.util.Date;

@Data
public class BaseModel {
    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date modifyTime;
}
