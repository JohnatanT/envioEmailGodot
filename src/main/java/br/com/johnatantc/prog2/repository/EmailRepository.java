package br.com.johnatantc.prog2.repository;

import br.com.johnatantc.prog2.entity.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmailRepository extends MongoRepository<Email, String> {

    List<Email> findByEnviado(Boolean enviado);
}
