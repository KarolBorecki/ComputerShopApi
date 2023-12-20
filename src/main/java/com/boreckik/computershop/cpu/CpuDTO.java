package com.boreckik.computershop.cpu;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CpuDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Double clock;

    private Long cpusProducent;

}
