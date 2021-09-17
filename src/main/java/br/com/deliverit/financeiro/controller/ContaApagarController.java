package br.com.deliverit.financeiro.controller;

import br.com.deliverit.financeiro.dto.ContaApagarRequest;
import br.com.deliverit.financeiro.dto.ContaApagarResponse;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.service.ContaApagarService;
import br.com.deliverit.financeiro.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class ContaApagarController {

    @Autowired
    private ContaApagarService service;

    @PostMapping
    public ResponseEntity<ContaApagarResponse> salvar(@Valid @RequestBody ContaApagarRequest contaApagarRequest) throws NegocioException {
        Optional<ContaApagarResponse> response = service.salvar(contaApagarRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

    @GetMapping
    public ResponseEntity<Page<ContaApagarResponse>> findAllPageable(WebRequest request) throws NegocioException {
        Pageable pageable = PageableUtil.getPageableParans(request.getParameterMap());
        Page response = service.findAllPageable(pageable);
        return response != null && response.getTotalElements() > 0 ? ResponseEntity.ok(response)
                : ResponseEntity.noContent().build();
    }
}
