package com.boreckik.computershop.computer;

import com.boreckik.computershop.buy.Buy;
import com.boreckik.computershop.buy.BuyRepository;
import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.cpu.CpuRepository;
import com.boreckik.computershop.gpu.Gpu;
import com.boreckik.computershop.gpu.GpuRepository;
import com.boreckik.computershop.util.NotFoundException;
import com.boreckik.computershop.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ComputerService {

    private final ComputerRepository computerRepository;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;
    private final BuyRepository buyRepository;

    public ComputerService(final ComputerRepository computerRepository,
            final CpuRepository cpuRepository, final GpuRepository gpuRepository,
            final BuyRepository buyRepository) {
        this.computerRepository = computerRepository;
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
        this.buyRepository = buyRepository;
    }

    public List<ComputerDTO> findAll() {
        final List<Computer> computers = computerRepository.findAll(Sort.by("id"));
        return computers.stream()
                .map(computer -> mapToDTO(computer, new ComputerDTO()))
                .toList();
    }

    public ComputerDTO get(final Long id) {
        return computerRepository.findById(id)
                .map(computer -> mapToDTO(computer, new ComputerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ComputerDTO computerDTO) {
        final Computer computer = new Computer();
        mapToEntity(computerDTO, computer);
        return computerRepository.save(computer).getId();
    }

    public void update(final Long id, final ComputerDTO computerDTO) {
        final Computer computer = computerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(computerDTO, computer);
        computerRepository.save(computer);
    }

    public void delete(final Long id) {
        computerRepository.deleteById(id);
    }

    private ComputerDTO mapToDTO(final Computer computer, final ComputerDTO computerDTO) {
        computerDTO.setId(computer.getId());
        computerDTO.setName(computer.getName());
        computerDTO.setPrice(computer.getPrice());
        computerDTO.setComputersCpu(computer.getComputersCpu() == null ? null : computer.getComputersCpu().getId());
        computerDTO.setComputersGpus(computer.getComputersGpus().stream()
                .map(gpu -> gpu.getId())
                .toList());
        return computerDTO;
    }

    private Computer mapToEntity(final ComputerDTO computerDTO, final Computer computer) {
        computer.setName(computerDTO.getName());
        computer.setPrice(computerDTO.getPrice());
        final Cpu computersCpu = computerDTO.getComputersCpu() == null ? null : cpuRepository.findById(computerDTO.getComputersCpu())
                .orElseThrow(() -> new NotFoundException("computersCpu not found"));
        computer.setComputersCpu(computersCpu);
        final List<Gpu> computersGpus = gpuRepository.findAllById(
                computerDTO.getComputersGpus() == null ? Collections.emptyList() : computerDTO.getComputersGpus());
        if (computersGpus.size() != (computerDTO.getComputersGpus() == null ? 0 : computerDTO.getComputersGpus().size())) {
            throw new NotFoundException("one of computersGpus not found");
        }
        computer.setComputersGpus(computersGpus.stream().collect(Collectors.toSet()));
        return computer;
    }

    public String getReferencedWarning(final Long id) {
        final Computer computer = computerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Buy buyComputerBuy = buyRepository.findFirstByBuyComputer(computer);
        if (buyComputerBuy != null) {
            return WebUtils.getMessage("computer.buy.buyComputer.referenced", buyComputerBuy.getId());
        }
        return null;
    }

}
