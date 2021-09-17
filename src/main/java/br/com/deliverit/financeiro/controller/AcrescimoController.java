package br.com.deliverit.financeiro.controller;

import br.com.deliverit.financeiro.dto.AcrescimoRequest;
import br.com.deliverit.financeiro.dto.AcrescimoResponse;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.service.AcrescimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/acrescimo")
public class AcrescimoController {

    @Autowired
    private AcrescimoService service;

    @PostMapping
    public ResponseEntity<AcrescimoResponse> salvar(@Valid @RequestBody AcrescimoRequest acrescimoRequest) {
        Optional<AcrescimoResponse> response = service.salvar(acrescimoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

    @GetMapping
    public ResponseEntity<AcrescimoResponse> findByNome(@RequestParam String nome) throws NegocioException {
        Optional<AcrescimoResponse> response = service.findByNome(nome);
        return response.isPresent() ? ResponseEntity.ok(response.get())
                : ResponseEntity.noContent().build();
    }
}
