package arthurrodrigues.monitorvagas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa uma vaga que já foi notificada ao usuário.
 * Utilizada para evitar notificações duplicadas e registrar as vagas já enviadas.
 */
@Entity
@Table(name = "vaga_recebida", uniqueConstraints = {
                @UniqueConstraint(name="fk_idusuario_urlvaga", columnNames = {"id_usuario", "url_vaga"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VagaRecebida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_usuario")
    @ManyToOne()
    private Usuario usuario;

    @Column(name = "titulo_vaga", nullable = false)
    private String tituloVaga;

    @Column(name = "url_vaga", nullable = false)
    private String urlVaga;
}