package com.boreckik.computershop.computer;

import com.boreckik.computershop.buy.Buy;
import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.gpu.Gpu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Computer {

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
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computers_cpu_id")
    private Cpu computersCpu;

    @OneToOne(mappedBy = "buyComputer", fetch = FetchType.LAZY)
    private Buy computerBuy;

    @ManyToMany
    @JoinTable(
            name = "ComputerGpus",
            joinColumns = @JoinColumn(name = "computerId"),
            inverseJoinColumns = @JoinColumn(name = "gpuId")
    )
    private Set<Gpu> computersGpus;

}
