package com.boreckik.computershop.computer;

import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.gpu.Gpu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComputerRepository extends JpaRepository<Computer, Long> {

    Computer findFirstByComputersCpu(Cpu cpu);

    Computer findFirstByComputersGpus(Gpu gpu);

    List<Computer> findAllByComputersGpus(Gpu gpu);

}
