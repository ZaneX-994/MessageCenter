package com.bytewizard.msgmate.tools;

import com.bytewizard.msgmate.enums.MsgStatus;
import com.bytewizard.msgmate.model.MsgRecordModel;
import com.bytewizard.msgmate.model.TemplateModel;
import com.bytewizard.msgmate.model.dto.SendMsgReq;

public interface MsgRecordService {

    MsgRecordModel GetMsgRecordWithCache(String msgId);

    void CreateMsgRecord(String msgId, SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status);

    void CreateOrUpdateMsgRecord(String msgId, SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status);
}
