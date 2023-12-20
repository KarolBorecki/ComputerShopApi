package com.boreckik.computershop.gpu;

import com.boreckik.computershop.producent.Producent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GpuRepository extends JpaRepository<Gpu, Long> {

    Gpu findFirstByGpusProducent(Producent producent);

}
