package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucb.CaresyncApp.entities.CodigoVerificacao;

import java.util.Optional;

@Repository
public interface CodigoVerificacaoRepository extends JpaRepository<CodigoVerificacao, Long> {

    Optional<CodigoVerificacao> findCodigoVerificacaoByEmailAndCodigoAndUsadoFalse(String email, String codigo);

}
