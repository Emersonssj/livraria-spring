package com.altislab.livraria.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    // 1. Tratamento para Erro 404 (Não Encontrado)
    // Acontece quando você faz repository.getReferenceById(id) e o ID não existe
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    // 2. Tratamento para Erro 400 (Dados Inválidos)
    // Acontece quando o Bean Validation (@NotBlank, @Email) falha no JSON enviado
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // 3. Tratamento para Regras de Negócio (Nossas Exceptions manuais)
    // Lembra dos "throw new RuntimeException" que colocamos no AluguelService?
    // Vamos capturar elas aqui para devolver 400 Bad Request com a mensagem.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity tratarErroRegraDeNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // DTO interno para formatar a resposta do erro 400
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}