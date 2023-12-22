package com.aurine.cloudx.push.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface PushStreams {
    String SMS_IN = "PUSH-SMS-IN";
    String SMS_OUT = "PUSH-SMS-OUT";
    String SMSS_IN = "PUSH-SMSS-IN";
    String SMSS_OUT = "PUSH-SMSS-OUT";
    
    String MAIL_IN = "PUSH-MAIL-IN";
    String MAIL_OUT = "PUSH-MAIL-OUT";
    String MAILS_IN = "PUSH-MAILS-IN";
    String MAILS_OUT = "PUSH-MAILS-OUT"; 
    
    String APP_IN = "PUSH-APP-IN";
    String APP_OUT = "PUSH-APP-OUT";
    String APPS_IN = "PUSH-APPS-IN";
    String APPS_OUT = "PUSH-APPS-OUT";

    String WX_IN = "WX-IN";
    String WX_OUT = "WX-OUT";

    @Input(SMS_IN)
    SubscribableChannel inboundSms();

    @Output(SMS_OUT)
    MessageChannel outboundSms();

    @Input(SMSS_IN)
    SubscribableChannel inboundSmss();

    @Output(SMSS_OUT)
    MessageChannel outboundSmss();


    @Input(MAIL_IN)
    SubscribableChannel inboundEmail();

    @Output(MAIL_OUT)
    MessageChannel outboundEmail();

    @Input(MAILS_IN)
    SubscribableChannel inboundEmails();

    @Output(MAILS_OUT)
    MessageChannel outboundEmails();
    
    
    @Input(APP_IN)
    SubscribableChannel inboundApp();

    @Output(APP_OUT)
    MessageChannel outboundApp();

    @Input(APPS_IN)
    SubscribableChannel inboundApps();

    @Output(APPS_OUT)
    MessageChannel outboundApps();


    @Input(WX_IN)
    SubscribableChannel inboundWx();

    @Output(WX_OUT)
    MessageChannel outboundWx();
}
