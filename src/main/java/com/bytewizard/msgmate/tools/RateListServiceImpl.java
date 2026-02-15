package com.bytewizard.msgmate.tools;

import org.springframework.stereotype.Service;

@Service
public class RateListServiceImpl implements RateLimitService{

    @Override
    public boolean isRequestAllowed(String sourceId, int channel, boolean isTimerMsg) {
        return false;
    }
}
