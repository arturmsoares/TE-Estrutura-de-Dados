package br.edu.iftm.estruturas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Atividade 2.1 - Trilha 2: Estruturas em Bancos de Dados
 * 
 * Aplicação Spring Boot para demonstração de:
 * • Índices B+ em bancos de dados relacionais
 * • Planos de execução SQL (INDEX SCAN vs TABLE SCAN)
 * • Análise de performance com e sem índices
 * 
 * Execução:
 * 1. mvn spring-boot:run
 * 2. Acessar http://localhost:8080/api/transacoes/info
 * 3. Acessar H2 Console: http://localhost:8080/h2-console
 */
@SpringBootApplication
public class Atividade2Application {

    public static void main(String[] args) {
        SpringApplication.run(Atividade2Application.class, args);
    }
}
