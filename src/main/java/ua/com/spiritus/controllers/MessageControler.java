package ua.com.spiritus.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.spiritus.models.SmsMessage;
import ua.com.spiritus.services.MessageService;

@RestController
@Log4j2
public class MessageControler {
    @Autowired
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/smsmessage/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsMessage> getUser(@RequestBody SmsMessage smsMessage) {
        log.info("Send message from sender with number {} to receiver with number {}", smsMessage.getSenderNumber(),
                smsMessage.getReceiverNumber());
        boolean sent = messageService.sendMessage(smsMessage);
        if (sent) {
            log.info("sms was sent");
            return new ResponseEntity<SmsMessage>(HttpStatus.OK);
        }
        else{
            log.warn("sms wasn't sent");
            return new ResponseEntity<SmsMessage>(HttpStatus.BAD_REQUEST);
        }

    }

}
