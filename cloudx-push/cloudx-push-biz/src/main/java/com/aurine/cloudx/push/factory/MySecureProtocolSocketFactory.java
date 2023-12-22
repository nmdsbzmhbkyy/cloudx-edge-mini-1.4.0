package com.aurine.cloudx.push.factory;

import com.aurine.cloudx.push.manager.MyX509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Slf4j
public class MySecureProtocolSocketFactory implements SecureProtocolSocketFactory {

    /**
     * ssl
     */
    private SSLContext sslContext = null;

    /**
     * Constructor for MySecureProtocolSocketFactory.
     */
    public MySecureProtocolSocketFactory() {
    }

    /**
     * 这个创建一个获取SSLContext的方法，导入MyX509TrustManager进行初始化
     *
     * @return
     */
    private static SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new MyX509TrustManager()},
                    null);
            return context;
        } catch (Exception e) {
            throw new HttpClientError(e.toString());
        }
    }

    /**
     * 判断获取SSLContext
     *
     * @return
     */
    private SSLContext getSSLContext() {
        if (this.sslContext == null) {
            this.sslContext = createEasySSLContext();
        }
        return this.sslContext;
    }

    /**
     *
     * @param host
     * @param port
     * @param clientHost
     * @param clientPort
     * @return
     * @throws IOException
     * @throws UnknownHostException
     */
    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    /**
     *
     * @param host
     * @param port
     * @param localAddress
     * @param localPort
     * @param params
     * @return
     * @throws IOException
     * @throws UnknownHostException
     * @throws ConnectTimeoutException
     */
    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
                               final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            log.error("params:{}",params);
            throw new IllegalArgumentException("参数不能为空");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        } else {
            return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
    }


    /**
     *
     * @param host
     * @param port
     * @return
     * @throws IOException
     * @throws UnknownHostException
     */
    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    /**
     *
     * @param socket
     * @param host
     * @param port
     * @param autoClose
     * @return
     * @throws IOException
     * @throws UnknownHostException
     */
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }
}