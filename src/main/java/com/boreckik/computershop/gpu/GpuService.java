package com.boreckik.computershop.gpu;

import com.boreckik.computershop.computer.Computer;
import com.boreckik.computershop.computer.ComputerRepository;
import com.boreckik.computershop.producent.Producent;
import com.boreckik.computershop.producent.ProducentRepository;
import com.boreckik.computershop.util.NotFoundException;
import com.boreckik.computershop.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class GpuService {

    private final GpuRepository gpuRepository;
    private final ProducentRepository producentRepository;
    private final ComputerRepository computerRepository;

    public GpuService(final GpuRepository gpuRepository,
            final ProducentRepository producentRepository,
            final ComputerRepository computerRepository) {
        this.gpuRepository = gpuRepository;
        this.producentRepository = producentRepository;
        this.computerRepository = computerRepository;
    }

    public List<GpuDTO> findAll() {
        final List<Gpu> gpus = gpuRepository.findAll(Sort.by("id"));
        return gpus.stream()
                .map(gpu -> mapToDTO(gpu, new GpuDTO()))
                .toList();
    }

    public GpuDTO get(final Long id) {
        return gpuRepository.findById(id)
                .map(gpu -> mapToDTO(gpu, new GpuDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GpuDTO gpuDTO) {
        final Gpu gpu = new Gpu();
        mapToEntity(gpuDTO, gpu);
        return gpuRepository.save(gpu).getId();
    }

    public void update(final Long id, final GpuDTO gpuDTO) {
        final Gpu gpu = gpuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(gpuDTO, gpu);
        gpuRepository.save(gpu);
    }

    public void delete(final Long id) {
        final Gpu gpu = gpuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        computerRepository.findAllByComputersGpus(gpu)
                .forEach(computer -> computer.getComputersGpus().remove(gpu));
        gpuRepository.delete(gpu);
    }

    private GpuDTO mapToDTO(final Gpu gpu, final GpuDTO gpuDTO) {
        gpuDTO.setId(gpu.getId());
        gpuDTO.setName(gpu.getName());
        gpuDTO.setMemory(gpu.getMemory());
        gpuDTO.setGpusProducent(gpu.getGpusProducent() == null ? null : gpu.getGpusProducent().getId());
        return gpuDTO;
    }

    private Gpu mapToEntity(final GpuDTO gpuDTO, final Gpu gpu) {
        gpu.setName(gpuDTO.getName());
        gpu.setMemory(gpuDTO.getMemory());
        final Producent gpusProducent = gpuDTO.getGpusProducent() == null ? null : producentRepository.findById(gpuDTO.getGpusProducent())
                .orElseThrow(() -> new NotFoundException("gpusProducent not found"));
        gpu.setGpusProducent(gpusProducent);
        return gpu;
    }

    public String getReferencedWarning(final Long id) {
        final Gpu gpu = gpuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Computer computersGpusComputer = computerRepository.findFirstByComputersGpus(gpu);
        if (computersGpusComputer != null) {
            return WebUtils.getMessage("gpu.computer.computersGpus.referenced", computersGpusComputer.getId());
        }
        return null;
    }

}
