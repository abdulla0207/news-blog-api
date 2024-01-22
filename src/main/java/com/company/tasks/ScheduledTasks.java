package com.company.tasks;

import com.company.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ConfirmationTokenService tokenService;

    @Scheduled(fixedRate = 60000)
    public void removeConfirmationTokens(){
        tokenService.removeExpiredAndUnconfirmedTokens();
    }
}
