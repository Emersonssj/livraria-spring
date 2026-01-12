package com.altislab.livraria.infra.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviarEmailRecuperacao(String destinatario, String token) {
        var mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Recuperação de Senha - Livraria AltisLab");
        mensagem.setText("Olá,\n\nVocê solicitou a recuperação de senha.\n" +
                "Use o seguinte token para redefinir sua senha: " + token + "\n\n" +
                "Este token expira em 30 minutos.");

        mailSender.send(mensagem);
    }
}