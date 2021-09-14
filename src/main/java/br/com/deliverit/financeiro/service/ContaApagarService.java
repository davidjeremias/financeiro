package br.com.deliverit.financeiro.service;

import br.com.deliverit.financeiro.dto.ContaApagarRequest;
import br.com.deliverit.financeiro.dto.ContaApagarResponse;
import br.com.deliverit.financeiro.entity.ContaApagar;
import br.com.deliverit.financeiro.repository.ContaApagarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaApagarService {

    @Autowired
    private ContaApagarRepository repository;

    @Autowired
    ModelMapper modelMapper;

    public Optional<ContaApagarResponse> salvar(ContaApagarRequest contaApagarRequest) {
        ContaApagar contaApagar = modelMapper.map(contaApagarRequest, ContaApagar.class);
        repository.save(contaApagar);
        return Optional.of(modelMapper.map(contaApagar, ContaApagarResponse.class));
    }
}
