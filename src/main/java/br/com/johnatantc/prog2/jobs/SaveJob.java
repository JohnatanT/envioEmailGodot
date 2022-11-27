package br.com.johnatantc.prog2.jobs;

import br.com.johnatantc.prog2.entity.Email;
import br.com.johnatantc.prog2.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@EnableScheduling
@Component
public class SaveJob {

    public static String modified;

    @Autowired
    private EmailRepository repository;

    @Scheduled(fixedDelay = 6000)
    public void atualizacaoSave() {
        Path path = Paths.get("C:\\Users\\johnatan.cavalcante\\AppData\\Roaming\\Godot\\app_userdata\\Save game demo (GDQuest)\\save.tres");
        FileTime fileTime;
        try {
            fileTime = Files.getLastModifiedTime(path);
            if (modified == null)
                modified = printFileTime(fileTime);
            else {
                String lastModified = printFileTime(fileTime);
                if (!lastModified.equals(modified)) {
                    modified = lastModified;
                    Email email = new Email();
                    email.setEnviado(false);
                    email.setAssunto("Novo Save Detectado!");
                    email.setMensagem("Seu jogo foi salvo com sucesso! Para mais informações do seu progresso verifique os dados em anexo do email.");
                    repository.save(email);
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot get the last modified time - " + e);
        }
    }

    private static String printFileTime(FileTime fileTime) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
        String data = dateFormat.format(fileTime.toMillis());
        System.out.println(data);

        return data;
    }
}
