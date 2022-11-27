package br.com.johnatantc.prog2.jobs;

import br.com.johnatantc.prog2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class EnvioEmailJob {

    @Autowired
    private EmailService service;

    @Scheduled(fixedDelay = 6000)
    public void enviar() {
        service.enviarEmails();
    }
}
