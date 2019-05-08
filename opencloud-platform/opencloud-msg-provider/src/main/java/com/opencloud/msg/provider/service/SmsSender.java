package com.opencloud.msg.provider.service;

import com.opencloud.msg.client.model.SmsNotify;

/**
 * @author woodev
 */
public interface SmsSender {

	/**
	 * 发送短信
	 * @param parameter
	 * @return
	 */
	Boolean send(SmsNotify parameter);
}
