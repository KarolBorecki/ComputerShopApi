package com.boreckik.computershop.producent;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/producents", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProducentResource {

    private final ProducentService producentService;

    public ProducentResource(final ProducentService producentService) {
        this.producentService = producentService;
    }

    @GetMapping
    public ResponseEntity<List<ProducentDTO>> getAllProducents() {
        return ResponseEntity.ok(producentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducentDTO> getProducent(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(producentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProducent(
            @RequestBody @Valid final ProducentDTO producentDTO) {
        final Long createdId = producentService.create(producentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProducent(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProducentDTO producentDTO) {
        producentService.update(id, producentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProducent(@PathVariable(name = "id") final Long id) {
        producentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
