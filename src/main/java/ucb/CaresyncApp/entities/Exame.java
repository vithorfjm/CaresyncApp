package ucb.CaresyncApp.entities;

import jakarta.persistence.*;
import ucb.CaresyncApp.DTOs.MarcacaoExameDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento_exames")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_exame")
    private String nomeExame;
    @Column(name = "data_exame")
    private LocalDateTime data;
    private String resultado;
    private String especialidade;
    private String local;
    private String endereco;
    private String observacoes;
    private String reavaliacao;
    @Column(name = "prescricoes_medicas")
    private String prescricoesMedicas;
    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private User paciente;
    @ManyToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private User medico;

    public Exame() {
    }

    public Exame(MarcacaoExameDTO dados, User paciente, User medico) {
        this.nomeExame = dados.nomeExame();
        this.data = dados.data().atTime(dados.hora());
        this.local = dados.localidade();
        this.endereco = dados.endereco();
        this.observacoes = dados.observacoes();
        this.paciente = paciente;
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeExame() {
        return nomeExame;
    }

    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getReavaliacao() {
        return reavaliacao;
    }

    public void setReavaliacao(String reavaliacao) {
        this.reavaliacao = reavaliacao;
    }

    public String getPrescricoesMedicas() {
        return prescricoesMedicas;
    }

    public void setPrescricoesMedicas(String prescricoesMedicas) {
        this.prescricoesMedicas = prescricoesMedicas;
    }

    public User getPaciente() {
        return paciente;
    }

    public void setPaciente(User paciente) {
        this.paciente = paciente;
    }

    public User getMedico() {
        return medico;
    }

    public void setMedico(User medico) {
        this.medico = medico;
    }
}
