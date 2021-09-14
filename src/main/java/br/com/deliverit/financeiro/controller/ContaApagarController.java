package br.com.deliverit.financeiro.controller;

import br.com.deliverit.financeiro.dto.ContaApagarRequest;
import br.com.deliverit.financeiro.dto.ContaApagarResponse;
import br.com.deliverit.financeiro.service.ContaApagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class ContaApagarController {

    @Autowired
    private ContaApagarService service;

    @PostMapping
    public ResponseEntity<ContaApagarResponse> salvar(@Valid @RequestBody ContaApagarRequest contaApagarRequest) {
        Optional<ContaApagarResponse> response = service.salvar(contaApagarRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

}
