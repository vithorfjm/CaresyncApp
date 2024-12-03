package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.CodigoValidoResponseDTO;
import ucb.CaresyncApp.entities.CodigoVerificacao;
import ucb.CaresyncApp.repositories.CodigoVerificacaoRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class CodigoVerificacaoService {

    @Autowired
    private UserService userService;

    @Autowired
    private CodigoVerificacaoRepository repository;

    public String gerarCodigoVerificacao(String email) {

        var emailExistente = userService.emailExiste(email);
        if (!emailExistente)
            return "";

        var codigo = String.format("%06d", new Random().nextInt(99999));
        var codigoVerificacao = new CodigoVerificacao();
        codigoVerificacao.setCodigo(codigo);
        codigoVerificacao.setEmail(email);
        codigoVerificacao.setDataDeExpiracao(gerarDataDeExpiracao());
        codigoVerificacao.setUsado(false);
        repository.save(codigoVerificacao);
        return codigo;
    }

    public CodigoValidoResponseDTO validarCodigo(String email, String codigo) {
        Optional<CodigoVerificacao> codigoOpt = repository.findCodigoVerificacaoByEmailAndCodigoAndUsadoFalse(email, codigo);
        if (codigoOpt.isPresent()) {
            var codigoVerificacao = codigoOpt.get();
            if(LocalDateTime.now().isBefore(codigoVerificacao.getDataDeExpiracao())) {
                return new CodigoValidoResponseDTO(true);
            }
        }
        return new CodigoValidoResponseDTO(false);
    }

    public CodigoValidoResponseDTO validarCodigo(String email, String codigo, String senha) {
        Optional<CodigoVerificacao> codigoOpt = repository.findCodigoVerificacaoByEmailAndCodigoAndUsadoFalse(email, codigo);
        if (codigoOpt.isPresent()) {
            var codigoVerificacao = codigoOpt.get();
            if(LocalDateTime.now().isBefore(codigoVerificacao.getDataDeExpiracao())) {
                codigoVerificacao.setUsado(true);
                repository.save(codigoVerificacao);
                return new CodigoValidoResponseDTO(true);
            }
        }
        return new CodigoValidoResponseDTO(false);
    }

    private LocalDateTime gerarDataDeExpiracao() {
        return LocalDateTime.now().plusMinutes(15);
    }
}
