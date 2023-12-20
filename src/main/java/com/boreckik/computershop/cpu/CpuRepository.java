package com.boreckik.computershop.cpu;

import com.boreckik.computershop.producent.Producent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CpuRepository extends JpaRepository<Cpu, Long> {

    Cpu findFirstByCpusProducent(Producent producent);

}
