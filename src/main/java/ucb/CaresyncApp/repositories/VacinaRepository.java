package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucb.CaresyncApp.entities.Vacina;

import java.util.List;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    List<Vacina> findByPacienteId(long pacienteId
    );

}
