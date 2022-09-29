package digit.service;

import digit.config.DTRConfiguration;
import digit.kafka.Producer;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import digit.web.models.SMSRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class NotificationService {

    @Autowired
    private Producer producer;

    @Autowired
    private DTRConfiguration config;

    @Autowired
    private RestTemplate restTemplate;

    private static final String smsTemplate = "Dear {APPLICANT_NAME} your death registration application has been successfully created on the system with application number - {APPNUMBER}.";

    public void prepareEventAndSend(DeathRegistrationRequest request){
        List<SMSRequest> smsRequestList = new ArrayList<>();
        request.getDeathRegistrationApplications().forEach(application -> {
            SMSRequest smsRequestForFather = SMSRequest.builder().mobileNumber(application.getApplicantMobileNumber()).message(getCustomMessage(smsTemplate, application)).build();
            smsRequestList.add(smsRequestForFather);
        });
        for (SMSRequest smsRequest : smsRequestList) {
            producer.push(config.getSmsNotificationTopic(), smsRequest);
//            log.info("Messages: " + smsRequest.getMessage());
        }
    }

    private String getCustomMessage(String template, DeathRegistrationApplication application) {
        template = template.replace("{APPNUMBER}", application.getApplicationNumber());
        template = template.replace("{APPLICANT_NAME}", application.getApplicant().getName());
        return template;
    }

}