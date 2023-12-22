package com.aurine.tools.eth.protocol.constants;

public final class EchoCodes {
    /**
     * 成功 0x00
     */
    public final static int Success = 0x00;

    /**
     * 失败/一般错误 0x01
     */
    public final static int Error = 0x01;
    /**
     * 忙应答 0x02
     */
    public final static int Busy = 0x02;

    /**
     * 不存在 0x03
     */
    public final static int NotExist = 0x03;

    /**
     * 无权限 0x04
     */
    public final static int PermissionDenied = 0x04;

    /**
     * 未申请该服务 0x05
     */
    public final static int ServiceNotApplied = 0x05;

    /**
     * 空间不足 0x06
     */
    public final static int NotEnoughSpace = 0x06;

    /**
     * 已存在 0x07
     */
    public final static int Exist = 0x07;

    /**
     * 请求方离线 0x08
     */
    public final static int Offline = 0x08;

    /**
     * MAC 错误或验证失败 0x09
     */
    public final static int MacError = 0x09;

    /**
     * 设置重复 0x0A
     */
    public final static int RepeatSet = 0x0A;

    /**
     * 控制失败 0x0B
     */
    public final static int CtrlFail = 0x0B;

    /**
     * 不支持该媒体参数 0x0C
     */
    public final static int UnsupportedParams = 0x0C;
}
