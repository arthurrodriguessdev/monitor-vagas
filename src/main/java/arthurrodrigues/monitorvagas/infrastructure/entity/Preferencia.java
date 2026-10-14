package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preferencia")
@AllArgsConstructor
@NoArgsConstructor
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    public Usuario usuario;
    public String nome;

    public Preferencia(Usuario usuario, String nome){
        this.usuario = usuario;
        this.nome = nome;
    }

    public Long getIdUsuario(){
        return this.usuario.getId();
    }
}