package com.boreckik.computershop.cpu;

import com.boreckik.computershop.computer.Computer;
import com.boreckik.computershop.producent.Producent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Cpu {

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

    @Column(nullable = false)
    private Double clock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpus_producent_id")
    private Producent cpusProducent;

    @OneToMany(mappedBy = "computersCpu")
    private Set<Computer> cpuComputers;

}
