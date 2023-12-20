package com.boreckik.computershop.producent;

import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.gpu.Gpu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Producent {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "gpusProducent")
    private Set<Gpu> producentGpu;

    @OneToMany(mappedBy = "cpusProducent")
    private Set<Cpu> producentCpu;

}
