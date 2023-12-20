package com.boreckik.computershop.buy;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BuyDTO {

    private Long id;

    @NotNull
    private LocalDate date;

    private Long buyComputer;

}
