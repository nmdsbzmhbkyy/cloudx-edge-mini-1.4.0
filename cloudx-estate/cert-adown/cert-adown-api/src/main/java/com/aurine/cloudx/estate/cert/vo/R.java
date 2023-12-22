package com.aurine.cloudx.estate.cert.vo;

import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel("响应信息主体")
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("返回标记：成功标记=0，失败标记=1")
	private int code;
	@ApiModelProperty("返回信息")
	private String msg;
	@ApiModelProperty("数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, (String) null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, (String) null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.FAIL, (String) null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.FAIL, (String) null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	public static <T> RBuilder<T> builder() {
		return new RBuilder();
	}

	@Override
	public String toString() {
		return "R(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
	}

	public R() {
	}

	public R(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public int getCode() {
		return this.code;
	}

	public R<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return this.msg;
	}

	public R<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return this.data;
	}

	public R<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static class RBuilder<T> {
		private int code;
		private String msg;
		private T data;

		RBuilder() {
		}

		public RBuilder<T> code(int code) {
			this.code = code;
			return this;
		}

		public RBuilder<T> msg(String msg) {
			this.msg = msg;
			return this;
		}

		public RBuilder<T> data(T data) {
			this.data = data;
			return this;
		}

		public R<T> build() {
			return new R(this.code, this.msg, this.data);
		}

		@Override
		public String toString() {
			return "R.RBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
		}
	}

}
