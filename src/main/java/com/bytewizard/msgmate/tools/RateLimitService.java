package com.bytewizard.msgmate.tools;

public interface RateLimitService {
    boolean isRequestAllowed(String sourceId,int channel,boolean isTimerMsg);
}
