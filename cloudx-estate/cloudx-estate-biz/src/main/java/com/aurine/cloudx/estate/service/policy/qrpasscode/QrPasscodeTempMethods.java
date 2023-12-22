package com.aurine.cloudx.estate.service.policy.qrpasscode;

public abstract class QrPasscodeTempMethods {

    private static final String REDIS_KEY = "REMOTE_QRCODE:%s:%s";


    protected abstract String getValidType();

    protected String getRedisKey(String passcode) {
        return String.format(REDIS_KEY, getValidType(), passcode);
    }

}
