package com.bytewizard.msgmate.tools;

import com.bytewizard.msgmate.common.conf.SendMsgConf;
import com.bytewizard.msgmate.constant.Constants;
import com.bytewizard.msgmate.mapper.GlobalQuotaMapper;
import com.bytewizard.msgmate.mapper.SourceQuotaMapper;
import com.bytewizard.msgmate.model.GlobalQuotaModel;
import com.bytewizard.msgmate.model.SourceQuotaModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RateListServiceImpl implements RateLimitService{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SendMsgConf sendMsgConf;

    @Autowired
    private SourceQuotaMapper sourceQuotaMapper;

    @Autowired
    private GlobalQuotaMapper globalQuotaMapper;


    @Override
    public boolean isRequestAllowed(String sourceId, int channel, boolean isTimerMsg) {


        // 先从渠道限额获取配置
        String quotaConf = getQuotaConfWithCache(channel,sourceId,isTimerMsg);
        if(StringUtils.isEmpty(quotaConf) || (quotaConf.split("_").length != 2)){
            return false;
        }

        String[] parts = quotaConf.split("_");
        int numLimit = Integer.parseInt(parts[0]);
        int unit = Integer.parseInt(parts[1]);

        // 根据配置，调用具体限额判断逻辑
        String keyId = String.format(Constants.REDIS_KEY_RATE_LIMIT_COUNT+":%s:%d", sourceId, channel);
        if(isTimerMsg){
            // 定时消息单独计数限频
            keyId = String.format(Constants.REDIS_KEY_RATE_LIMIT_COUNT_TIMER+":%s:%d", sourceId, channel);
        }

        return checkAllowed(keyId, numLimit, unit);
    }

    private String getQuotaConfWithCache(int channel, String sourceId, boolean isTimerMsg) {

        String quotaCacheKey = Constants.REDIS_KEY_SOURCE_QUOTA + sourceId + channel;
        String cacheQt = redisTemplate.opsForValue().get(quotaCacheKey);

        String rsQuotaStr = "";

        // 1. 开启缓存且有值，直接返回
        if(!StringUtils.isEmpty(cacheQt) && sendMsgConf.isOpenCache()){
            rsQuotaStr = cacheQt;
            return rsQuotaStr;
        } else {
            //2.从数据库获取配置

            //先从渠道限额获取配置
            SourceQuotaModel sourceQuota = sourceQuotaMapper.getSourceQuota(channel, sourceId);
            if(sourceQuota != null){
                rsQuotaStr = sourceQuota.getNum() + "_" + sourceQuota.getUnit();
            } else {
                // 如果没有配置渠道限额，那就从全局限额获取配置
                GlobalQuotaModel globalQuota = globalQuotaMapper.getGlobalQuota(channel);
                if (globalQuota != null){
                    rsQuotaStr = globalQuota.getNum() + "_" + globalQuota.getUnit();
                }
            }
        }

        // 缓存起来
        if(sendMsgConf.isOpenCache() && !StringUtils.isEmpty(rsQuotaStr)){
            redisTemplate.opsForValue().set(quotaCacheKey, rsQuotaStr, Duration.ofSeconds(30));
        }

        return rsQuotaStr;

    }

    public boolean checkAllowed(String keyId, int limit, long div) {
        long currentStart = System.currentTimeMillis() / div;
        String key = String.format(keyId+":%d", currentStart);

        // 递增计数器
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == null){
            log.error("增加计数失败");
            return  false;
        }
        // 如果是第一次累加，则设置一个时间一秒限时
        if (count == 1) {
            long expireSeconds = div / 1000;
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }

        // 比较检查是否超过限制次数limit
        return count <= limit;
    }

}
