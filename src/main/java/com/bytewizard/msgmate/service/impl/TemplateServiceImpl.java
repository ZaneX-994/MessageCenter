package com.bytewizard.msgmate.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.bytewizard.msgmate.common.conf.SendMsgConf;
import com.bytewizard.msgmate.constant.Constants;
import com.bytewizard.msgmate.constant.SnowflakeConstant;
import com.bytewizard.msgmate.exception.BusinessException;
import com.bytewizard.msgmate.exception.ErrorCode;
import com.bytewizard.msgmate.mapper.TemplateMapper;
import com.bytewizard.msgmate.model.TemplateModel;
import com.bytewizard.msgmate.service.TemplateService;
import com.bytewizard.msgmate.utils.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    SendMsgConf sendMsgConf;

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Override
    public String CreateTemplate(TemplateModel templateModel) {
        // 校验参数
        if(templateModel.getChannel() == 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"校验 chanenl 参数出错");
        }

        //其他参数校验，略

        // 生成模板 ID
        Snowflake snowflake = IdUtil.getSnowflake(SnowflakeConstant.WORKER_ID, SnowflakeConstant.DATA_CENTER_ID);
        templateModel.setTemplateId(snowflake.nextId() + "");

        // 存入数据库
        templateMapper.save(templateModel);
        return templateModel.getTemplateId();
    }

    @Override
    public void DeleteTemplate(String templateID) {
        templateMapper.deleteById(templateID);
    }

    @Override
    public void UpdateTemplate(TemplateModel templateModel) {
        templateMapper.update(templateModel);
    }

    @Override
    public TemplateModel GetTemplate(String templateID) {
        return templateMapper.getTemplateById(templateID);
    }

    @Override
    public TemplateModel GetTemplateWithCache(String templateID) {
        String templateCacheKey = Constants.REDIS_KEY_TEMPLATE+templateID;
        String cacheTp = redisTemplate.opsForValue().get(templateCacheKey);

        TemplateModel tp = null;
        if(!StringUtils.isEmpty(cacheTp) && sendMsgConf.isOpenCache()){
            tp = JSONUtil.parseObject(cacheTp,TemplateModel.class);
            if(tp != null){
                return tp;
            }
        }

        // 从数据库获取
        tp = templateMapper.getTemplateById(templateID);

        // 存入缓存
        redisTemplate.opsForValue().set(templateCacheKey,JSONUtil.toJsonString(tp), Duration.ofSeconds(30));

        return tp;
    }
}
