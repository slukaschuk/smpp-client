package ua.com.spiritus.services;


import ua.com.spiritus.models.SmsMessage;

public interface MessageService {
    public boolean sendMessage(SmsMessage smsMessage);
}
