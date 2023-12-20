package com.boreckik.computershop.producent;

import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.cpu.CpuRepository;
import com.boreckik.computershop.gpu.Gpu;
import com.boreckik.computershop.gpu.GpuRepository;
import com.boreckik.computershop.util.NotFoundException;
import com.boreckik.computershop.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProducentService {

    private final ProducentRepository producentRepository;
    private final GpuRepository gpuRepository;
    private final CpuRepository cpuRepository;

    public ProducentService(final ProducentRepository producentRepository,
            final GpuRepository gpuRepository, final CpuRepository cpuRepository) {
        this.producentRepository = producentRepository;
        this.gpuRepository = gpuRepository;
        this.cpuRepository = cpuRepository;
    }

    public List<ProducentDTO> findAll() {
        final List<Producent> producents = producentRepository.findAll(Sort.by("id"));
        return producents.stream()
                .map(producent -> mapToDTO(producent, new ProducentDTO()))
                .toList();
    }

    public ProducentDTO get(final Long id) {
        return producentRepository.findById(id)
                .map(producent -> mapToDTO(producent, new ProducentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProducentDTO producentDTO) {
        final Producent producent = new Producent();
        mapToEntity(producentDTO, producent);
        return producentRepository.save(producent).getId();
    }

    public void update(final Long id, final ProducentDTO producentDTO) {
        final Producent producent = producentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(producentDTO, producent);
        producentRepository.save(producent);
    }

    public void delete(final Long id) {
        producentRepository.deleteById(id);
    }

    private ProducentDTO mapToDTO(final Producent producent, final ProducentDTO producentDTO) {
        producentDTO.setId(producent.getId());
        producentDTO.setName(producent.getName());
        return producentDTO;
    }

    private Producent mapToEntity(final ProducentDTO producentDTO, final Producent producent) {
        producent.setName(producentDTO.getName());
        return producent;
    }

    public String getReferencedWarning(final Long id) {
        final Producent producent = producentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Gpu gpusProducentGpu = gpuRepository.findFirstByGpusProducent(producent);
        if (gpusProducentGpu != null) {
            return WebUtils.getMessage("producent.gpu.gpusProducent.referenced", gpusProducentGpu.getId());
        }
        final Cpu cpusProducentCpu = cpuRepository.findFirstByCpusProducent(producent);
        if (cpusProducentCpu != null) {
            return WebUtils.getMessage("producent.cpu.cpusProducent.referenced", cpusProducentCpu.getId());
        }
        return null;
    }

}
