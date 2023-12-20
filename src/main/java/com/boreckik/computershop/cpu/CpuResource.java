package com.boreckik.computershop.cpu;

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
@RequestMapping(value = "/api/cpus", produces = MediaType.APPLICATION_JSON_VALUE)
public class CpuResource {

    private final CpuService cpuService;

    public CpuResource(final CpuService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public ResponseEntity<List<CpuDTO>> getAllCpus() {
        return ResponseEntity.ok(cpuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuDTO> getCpu(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cpuService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCpu(@RequestBody @Valid final CpuDTO cpuDTO) {
        final Long createdId = cpuService.create(cpuDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCpu(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CpuDTO cpuDTO) {
        cpuService.update(id, cpuDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCpu(@PathVariable(name = "id") final Long id) {
        cpuService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
