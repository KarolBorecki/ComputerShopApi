package com.boreckik.computershop.producent;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProducentDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

}
