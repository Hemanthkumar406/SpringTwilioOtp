package com.Otp.TwilioOtp.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Otp.TwilioOtp.Config.TwilioConfig;
import com.Otp.TwilioOtp.Dto.OtpRequest;
import com.Otp.TwilioOtp.Dto.OtpResponse;
import com.Otp.TwilioOtp.Dto.OtpStatus;
import com.Otp.TwilioOtp.Dto.OtpValidationRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

	
	@Autowired
	private TwilioConfig config;

	Map<String, String> otpMap = new HashMap<>();
	
	public OtpResponse sendSMS(OtpRequest request)
	{
		OtpResponse oResponse = null;
		
		try
		{
			
			PhoneNumber to = new PhoneNumber(request.getPhoneNumber());
			PhoneNumber from = new PhoneNumber(config.getPhoneNumber());
			String otp = generateOTP();
			String otpMessage = "Dear Datta Your Phone Hack Now  ,   " + otp + " Plzz Click This Link. "+" "+"https://pranx.com/hacker/";
			Message message = Message
			        .creator(to, from,
			                otpMessage)
			        .create();
			otpMap.put(request.getUsername(),otp);
			oResponse = new OtpResponse(OtpStatus.DELIVERED,otpMessage);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			oResponse = new OtpResponse(OtpStatus.FAILED,e.getMessage());
		}
		return oResponse;
	}
	
	public String validateOtp(OtpValidationRequest otpValidationRequest) {
		Set<String> keys = otpMap.keySet();
		String username = null;
		for(String key : keys)
			username = key;
        if (otpValidationRequest.getUsername().equals(username)) {
            otpMap.remove(username,otpValidationRequest.getOtpNumber());
            return "OTP is valid!";
        } else {
            return "OTP is invalid!";
        }
	}

	private String generateOTP() {
		return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
	}
}
