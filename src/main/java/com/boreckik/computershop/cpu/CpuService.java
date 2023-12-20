package com.boreckik.computershop.cpu;

import com.boreckik.computershop.computer.Computer;
import com.boreckik.computershop.computer.ComputerRepository;
import com.boreckik.computershop.producent.Producent;
import com.boreckik.computershop.producent.ProducentRepository;
import com.boreckik.computershop.util.NotFoundException;
import com.boreckik.computershop.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CpuService {

    private final CpuRepository cpuRepository;
    private final ProducentRepository producentRepository;
    private final ComputerRepository computerRepository;

    public CpuService(final CpuRepository cpuRepository,
            final ProducentRepository producentRepository,
            final ComputerRepository computerRepository) {
        this.cpuRepository = cpuRepository;
        this.producentRepository = producentRepository;
        this.computerRepository = computerRepository;
    }

    public List<CpuDTO> findAll() {
        final List<Cpu> cpus = cpuRepository.findAll(Sort.by("id"));
        return cpus.stream()
                .map(cpu -> mapToDTO(cpu, new CpuDTO()))
                .toList();
    }

    public CpuDTO get(final Long id) {
        return cpuRepository.findById(id)
                .map(cpu -> mapToDTO(cpu, new CpuDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CpuDTO cpuDTO) {
        final Cpu cpu = new Cpu();
        mapToEntity(cpuDTO, cpu);
        return cpuRepository.save(cpu).getId();
    }

    public void update(final Long id, final CpuDTO cpuDTO) {
        final Cpu cpu = cpuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cpuDTO, cpu);
        cpuRepository.save(cpu);
    }

    public void delete(final Long id) {
        cpuRepository.deleteById(id);
    }

    private CpuDTO mapToDTO(final Cpu cpu, final CpuDTO cpuDTO) {
        cpuDTO.setId(cpu.getId());
        cpuDTO.setName(cpu.getName());
        cpuDTO.setClock(cpu.getClock());
        cpuDTO.setCpusProducent(cpu.getCpusProducent() == null ? null : cpu.getCpusProducent().getId());
        return cpuDTO;
    }

    private Cpu mapToEntity(final CpuDTO cpuDTO, final Cpu cpu) {
        cpu.setName(cpuDTO.getName());
        cpu.setClock(cpuDTO.getClock());
        final Producent cpusProducent = cpuDTO.getCpusProducent() == null ? null : producentRepository.findById(cpuDTO.getCpusProducent())
                .orElseThrow(() -> new NotFoundException("cpusProducent not found"));
        cpu.setCpusProducent(cpusProducent);
        return cpu;
    }

    public String getReferencedWarning(final Long id) {
        final Cpu cpu = cpuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Computer computersCpuComputer = computerRepository.findFirstByComputersCpu(cpu);
        if (computersCpuComputer != null) {
            return WebUtils.getMessage("cpu.computer.computersCpu.referenced", computersCpuComputer.getId());
        }
        return null;
    }

}
