package com.boreckik.computershop.buy;

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
@RequestMapping(value = "/api/buys", produces = MediaType.APPLICATION_JSON_VALUE)
public class BuyResource {

    private final BuyService buyService;

    public BuyResource(final BuyService buyService) {
        this.buyService = buyService;
    }

    @GetMapping
    public ResponseEntity<List<BuyDTO>> getAllBuys() {
        return ResponseEntity.ok(buyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyDTO> getBuy(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(buyService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBuy(@RequestBody @Valid final BuyDTO buyDTO) {
        final Long createdId = buyService.create(buyDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBuy(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BuyDTO buyDTO) {
        buyService.update(id, buyDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBuy(@PathVariable(name = "id") final Long id) {
        buyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
