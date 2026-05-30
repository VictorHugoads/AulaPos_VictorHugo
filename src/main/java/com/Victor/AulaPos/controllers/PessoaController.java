package com.Victor.AulaPos.controllers;

import com.Victor.AulaPos.models.Pessoa;
import com.Victor.AulaPos.repositories.PessoaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Pessoa> getPessoas() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pessoa getPessoa(@PathVariable Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pessoa não encontrada"
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa adicionar(@RequestBody Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    // PUT /pessoa/{id}
    // Substitui tudo: id e nome
    @PutMapping("/{id}")
    public Pessoa substituirPessoa(@PathVariable Long id, @RequestBody Pessoa novaPessoa) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pessoa não encontrada"
                ));

        pessoa.setId(id);
        pessoa.setNome(novaPessoa.getNome());

        return pessoaRepository.save(pessoa);
    }

    // PATCH /pessoa/{id}
    // Atualiza parcial, por exemplo só o nome
    @PatchMapping("/{id}")
    public Pessoa atualizarParcial(@PathVariable Long id, @RequestBody Pessoa novaPessoa) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pessoa não encontrada"
                ));

        if (novaPessoa.getNome() != null) {
            pessoa.setNome(novaPessoa.getNome());
        }

        return pessoaRepository.save(pessoa);
    }

    // DELETE /pessoa/{id}
    // Remove e retorna 204
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePessoa(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pessoa não encontrada"
            );
        }

        pessoaRepository.deleteById(id);
    }
}