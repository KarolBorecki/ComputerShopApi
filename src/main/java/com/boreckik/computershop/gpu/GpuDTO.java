package com.boreckik.computershop.gpu;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GpuDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Long memory;

    private Long gpusProducent;

}
