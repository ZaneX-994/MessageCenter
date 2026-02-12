package com.bytewizard.msgmate.contoller;

import com.bytewizard.msgmate.model.TemplateModel;
import com.bytewizard.msgmate.service.SendMsgService;
import com.bytewizard.msgmate.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
@Slf4j
public class MsgCenterController {

    @Resource
    private TemplateService templateService;

    @Resource
    private SendMsgService sendMsgService;


    @PostMapping(value = "/create_template")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateModel templateModel){
        String templateId = templateService.CreateTemplate(templateModel);
        return ResponseEntity.ok(templateId);
    }
}
