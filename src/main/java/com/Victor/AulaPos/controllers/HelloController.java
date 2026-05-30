package com.Victor.AulaPos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class HelloController {

    // =========================
    // ROTAS 
    // =========================

    @GetMapping("/hello/hello")
    public String hello() {
        return "HELLO World!";
    }

    @GetMapping("/hello/world")
    public String world() {
        return "Hello WORLD!";
    }

    @PostMapping("/hello/world/{valor}")
    @ResponseStatus(HttpStatus.CREATED)
    public String retornaValor(@PathVariable String valor) {
        return valor;
    }

    @PostMapping("/hello/soma/{valor1}")
    @ResponseStatus(HttpStatus.CREATED)
    public int retornaSoma(@PathVariable int valor1, @RequestBody int valor2) {
        return valor1 + valor2;
    }

    // =========================
    // EXERCÍCIO 1
    // GET /hello/{nome}
    // =========================

    @GetMapping("/hello/{nome}")
    public String retornaNome(@PathVariable String nome) {
        return "Olá, " + nome + "!";
    }

    // =========================
    // GET /calc/soma?a=10&b=5
    // =========================

    @GetMapping("/calc/soma")
    public Map<String, Integer> somaPorParametro(
            @RequestParam(required = false) Integer a,
            @RequestParam(required = false) Integer b) {

        if (a == null || b == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os parâmetros 'a' e 'b' são obrigatórios."
            );
        }

        return Map.of("resultado", a + b);
    }

    // =========================
    // GET /calc/soma/{a}/{b}
    // =========================

    @GetMapping("/calc/soma/{a}/{b}")
    public Map<String, Integer> somaPorPath(
            @PathVariable int a,
            @PathVariable int b) {

        return Map.of("resultado", a + b);
    }

    // Se faltar o b na rota /calc/soma/{a}/{b}
    @GetMapping("/calc/soma/{a}")
    public Map<String, String> erroSomaPath(@PathVariable int a) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Os valores 'a' e 'b' são obrigatórios."
        );
    }

    // =========================
    // GET /temperatura/convert?valor=30&de=C&para=F
    // =========================

    @GetMapping("/temperatura/convert")
    public Map<String, Double> converterTemperaturaPorParametro(
            @RequestParam(required = false) Double valor,
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String para) {

        if (valor == null || de == null || para == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os parâmetros 'valor', 'de' e 'para' são obrigatórios."
            );
        }

        return calcularTemperatura(valor, de, para);
    }

    // =========================
    // GET /temperatura/{valor}/{de}/{para}
    // Exemplo: /temperatura/30/C/F
    // =========================

    @GetMapping("/temperatura/{valor}/{de}/{para}")
    public Map<String, Double> converterTemperaturaPorPath(
            @PathVariable Double valor,
            @PathVariable String de,
            @PathVariable String para) {

        return calcularTemperatura(valor, de, para);
    }

    // Método auxiliar para calcular temperatura
    private Map<String, Double> calcularTemperatura(Double valor, String de, String para) {

        de = de.toUpperCase();
        para = para.toUpperCase();

        if (!formatoValido(de) || !formatoValido(para)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Os valores de 'de' e 'para' devem ser C, F ou K."
            );
        }

        double valorEmCelsius;

        if (de.equals("C")) {
            valorEmCelsius = valor;
        } else if (de.equals("F")) {
            valorEmCelsius = (valor - 32) * 5 / 9;
        } else {
            valorEmCelsius = valor - 273.15;
        }

        double resultado;

        if (para.equals("C")) {
            resultado = valorEmCelsius;
        } else if (para.equals("F")) {
            resultado = valorEmCelsius * 9 / 5 + 32;
        } else {
            resultado = valorEmCelsius + 273.15;
        }

        return Map.of("resultado", resultado);
    }

    private boolean formatoValido(String formato) {
        return formato.equals("C") || formato.equals("F") || formato.equals("K");
    }
}