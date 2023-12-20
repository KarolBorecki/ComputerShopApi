package com.boreckik.computershop.computer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ComputerDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Long price;

    private Long computersCpu;

    private List<Long> computersGpus;

}
