package com.boreckik.computershop.computer;

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
@RequestMapping(value = "/api/computers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComputerResource {

    private final ComputerService computerService;

    public ComputerResource(final ComputerService computerService) {
        this.computerService = computerService;
    }

    @GetMapping
    public ResponseEntity<List<ComputerDTO>> getAllComputers() {
        return ResponseEntity.ok(computerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComputerDTO> getComputer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(computerService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createComputer(@RequestBody @Valid final ComputerDTO computerDTO) {
        final Long createdId = computerService.create(computerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateComputer(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ComputerDTO computerDTO) {
        computerService.update(id, computerDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComputer(@PathVariable(name = "id") final Long id) {
        computerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
