package ua.com.spiritus.models;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SmsMessage {
    @NonNull
    private String senderNumber;
    @NonNull
    private String receiverNumber;
    @NonNull
    private String message;
}
