package com.sajith.flowcontroller.throttle.rule;
import com.sajith.flowcontroller.throttle.common.ControlEvent;


import java.util.HashMap;
import java.util.Map;

public class QuotaBasedRule implements Rule {
    Map<String, Quota> senderQuota = new HashMap<>();
    private static final int QUOTA_PERIOD_MILLS = 60000;

    @Override
    public boolean evaluate(String key) {
        boolean isBlocked = false;
        Quota quota = senderQuota.get(key);
        if (quota != null) {
            isBlocked = quota.increment();
        }
        return isBlocked;
    }

    @Override
    public void onEvent(ControlEvent event) {
        if (event.getType().equals(ControlEvent.QUOTA_SET_MESSAGE_TYPE)) {
            String key = event.getKey();
            int quotaValue = event.getQuota();
            Quota quota = senderQuota.get(key);
            if (quota == null) {
                quota = new Quota(quotaValue, QUOTA_PERIOD_MILLS);
                senderQuota.put(key, quota);
            }
            quota.setQuota(quotaValue);
        }
    }

    private class Quota {
        private int quota;
        private int consumed;
        private long lastResetTimestamp;
        private int quotaPeriodMills;

        public Quota(int quota, int quotaPeriodMills){
            this.quota = quota;
            this.quotaPeriodMills = quotaPeriodMills;
        }

        public void setQuota(int quota) {
            this.quota = quota;
        }

        public boolean increment() {
            Long timeNow = System.currentTimeMillis();
            Long timeDiff = timeNow - lastResetTimestamp;
            if (timeDiff >= quotaPeriodMills){
                reset(timeNow);
            }
            consumed++;
            return (consumed > quota);
        }

        private void reset(Long timeNow) {
            this.lastResetTimestamp = timeNow;
            this.consumed = 0;
        }
    }
}
