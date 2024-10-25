package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucb.CaresyncApp.entities.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
