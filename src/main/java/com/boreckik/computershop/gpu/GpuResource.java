package com.boreckik.computershop.gpu;

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
@RequestMapping(value = "/api/gpus", produces = MediaType.APPLICATION_JSON_VALUE)
public class GpuResource {

    private final GpuService gpuService;

    public GpuResource(final GpuService gpuService) {
        this.gpuService = gpuService;
    }

    @GetMapping
    public ResponseEntity<List<GpuDTO>> getAllGpus() {
        return ResponseEntity.ok(gpuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GpuDTO> getGpu(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(gpuService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createGpu(@RequestBody @Valid final GpuDTO gpuDTO) {
        final Long createdId = gpuService.create(gpuDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateGpu(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final GpuDTO gpuDTO) {
        gpuService.update(id, gpuDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGpu(@PathVariable(name = "id") final Long id) {
        gpuService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
