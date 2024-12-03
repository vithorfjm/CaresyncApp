package ucb.CaresyncApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.AlterarSenhaRequestDTO;
import ucb.CaresyncApp.DTOs.recuperacao_de_senha.CodigoValidoResponseDTO;

@Service
public class RecuperacaoDeSenhaService {

    @Autowired
    private CodigoVerificacaoService codigoVerificacaoService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;

    public void enviarEmail(String email) {
        var codigo = codigoVerificacaoService.gerarCodigoVerificacao(email);
        if (codigo.isEmpty()) {
            return;
        }
        var nomePaciente = userService.pegarNomeDeUsuarioPeloEmail(email);
        emailSenderService.enviarEmail(email, codigo, nomePaciente);
    }

    public CodigoValidoResponseDTO validarCodigo(String email, String codigo) {
        return codigoVerificacaoService.validarCodigo(email, codigo);
    }

    public void alterarSenha(AlterarSenhaRequestDTO dados) {
        var validacao = codigoVerificacaoService.validarCodigo(dados.email(), dados.codigo(), dados.senha());
        if (validacao.codigoValido()) {
            userService.trocarDeSenha(dados.email(), dados.senha());
        }
    }




}
