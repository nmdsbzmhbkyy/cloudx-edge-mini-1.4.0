package com.aurine.cloudx.estate.cert.exception;


import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.exception.code.ErrorCode;

/**
 * @description: 业务异常
 * @author: wangwei
 * @date: 2021/12/16 18:21
 **/
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 5565760508056698922L;

	/**
	 * 异常代码
	 */
	private ErrorCode errorCode;


	/**
	 * 当前请求
	 */
	private SysCertAdownRequest certAdownRequest;

	public BusinessException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, SysCertAdownRequest certAdownRequest) {
		super();
		this.errorCode = errorCode;
		this.certAdownRequest = certAdownRequest;
	}

	public BusinessException() {
		super();
	}

	public BusinessException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public BusinessException(ErrorCode errorCode, String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		this.errorCode = errorCode;
	}

	public BusinessException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BusinessException(ErrorCode errorCode, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errorCode = errorCode;
	}

	public BusinessException(String arg0) {
		super(arg0);
	}

	public BusinessException(ErrorCode errorCode, String arg0) {
		super(arg0);
		this.errorCode = errorCode;
	}

	public BusinessException(Throwable arg0) {
		super(arg0);
	}

	public BusinessException(ErrorCode errorCode, Throwable arg0) {
		super(arg0);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
