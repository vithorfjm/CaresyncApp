package ucb.CaresyncApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ucb.CaresyncApp.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
    List<User> findByEspecialidade(String especialidade);

    @Query("SELECT u FROM User u WHERE u.especialidade = :especialidade AND u NOT IN " +
            "(SELECT c.medico FROM Consulta c WHERE c.dataConsulta BETWEEN :vinteMinAntes AND :vinteMinDepois)")
    List<User> findMedicosDisponiveisPorEspecialidadeEData(@Param("especialidade") String especialidade,
                                                           @Param("vinteMinAntes") LocalDateTime vinteMinAntes,
                                                           @Param("vinteMinDepois") LocalDateTime vinteMinDepois);
}
