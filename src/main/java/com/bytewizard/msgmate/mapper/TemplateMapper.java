package com.bytewizard.msgmate.mapper;

import com.bytewizard.msgmate.model.TemplateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TemplateMapper {

    void save(@Param("templateModel") TemplateModel templateModel);

    void deleteById(@Param("templateId") String templateId);

    void update(@Param("templateModel") TemplateModel templateModel);

    TemplateModel getTemplateById(@Param("templateId") String templateId);

}
