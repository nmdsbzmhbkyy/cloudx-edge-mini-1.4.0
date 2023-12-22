package com.aurine.cloudx.push.manager;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyX509TrustManager implements X509TrustManager {
    /**
     *
     * @param arg0
     * @param arg1
     * @throws CertificateException
     */
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }

    /**
     *
     * @param arg0
     * @param arg1
     * @throws CertificateException
     */
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }

    /**
     *
     * @return
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}