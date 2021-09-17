package br.com.deliverit.financeiro.service;

import br.com.deliverit.financeiro.dto.AcrescimoRequest;
import br.com.deliverit.financeiro.dto.AcrescimoResponse;
import br.com.deliverit.financeiro.entity.Acrescimo;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.repository.AcrescimoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AcrescimoService {

    @Autowired
    private AcrescimoRepository repository;

    @Autowired
    ModelMapper modelMapper;

    public Optional<AcrescimoResponse> salvar(AcrescimoRequest acrescimoRequest) {
        Acrescimo acrescimo = modelMapper.map(acrescimoRequest, Acrescimo.class);
        AcrescimoResponse acrescimoResponse = modelMapper.map(repository.save(acrescimo), AcrescimoResponse.class);
        return Optional.of(acrescimoResponse);
    }

    public Optional<AcrescimoResponse> findByNome(String nome) throws NegocioException {
        Acrescimo acrescimo = repository.findByNome(nome);
        if (acrescimo == null)
            throw new NegocioException("Regra de acrescimo não localizada");
        return Optional.of(modelMapper.map(acrescimo, AcrescimoResponse.class));
    }
}
