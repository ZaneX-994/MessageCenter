package com.bytewizard.msgmate.service;

import com.bytewizard.msgmate.model.TemplateModel;

public interface TemplateService {

    String CreateTemplate(TemplateModel templateModel);

    void DeleteTemplate(String templateID);

    void UpdateTemplate(TemplateModel templateModel);

    TemplateModel GetTemplate(String templateID);

    TemplateModel GetTemplateWithCache(String templateID);
}
