package arthurrodrigues.monitorvagas.business.controller;

import arthurrodrigues.monitorvagas.business.JobService;
import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vagas")
public class VagasController {
    private final JobService jobService;

    @GetMapping("/pesquisar")
    public ResponseEntity<List<JobDTO>> pesquisarVagas(@RequestBody(required = false) List<String> listaPreferencias,
                                                       @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(jobService.pesquisarVagas(listaPreferencias));
    }
}
