package com.forest.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.forest.bos.utils.SmsUtils;

@Service
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 获取MapMessage
		MapMessage mapMessage = (MapMessage) message;
		// 调用utils发送短信
		try {
			/*SendSmsResponse sendSms = SmsUtils.sendSms(mapMessage.getString("telephone"),
					mapMessage.getString("checkcode"));*/
			SendSmsResponse sendSms = new SendSmsResponse();
			sendSms.setCode("OK");
			System.out.println(mapMessage.getString("telephone")+"------"+mapMessage.getString("checkcode"));
			if (sendSms.getCode()!=null&&sendSms.getCode().equals("OK")) {
				//发送成功
				System.out.println(sendSms.getMessage()+"-------"+sendSms.getRequestId());
			}else {
				//失败
				throw new RuntimeException("发送信息失败，信息码："+sendSms.getCode());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
