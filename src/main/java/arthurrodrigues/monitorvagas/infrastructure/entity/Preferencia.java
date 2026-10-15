package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "preferencia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    private String nome;

    public Preferencia(Usuario usuario, String nome){
        this.usuario = usuario;
        this.nome = nome;
    }

    public Long getIdUsuario(){
        return this.usuario.getId();
    }
}