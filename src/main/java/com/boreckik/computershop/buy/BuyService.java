package com.boreckik.computershop.buy;

import com.boreckik.computershop.computer.Computer;
import com.boreckik.computershop.computer.ComputerRepository;
import com.boreckik.computershop.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BuyService {

    private final BuyRepository buyRepository;
    private final ComputerRepository computerRepository;

    public BuyService(final BuyRepository buyRepository,
            final ComputerRepository computerRepository) {
        this.buyRepository = buyRepository;
        this.computerRepository = computerRepository;
    }

    public List<BuyDTO> findAll() {
        final List<Buy> buys = buyRepository.findAll(Sort.by("id"));
        return buys.stream()
                .map(buy -> mapToDTO(buy, new BuyDTO()))
                .toList();
    }

    public BuyDTO get(final Long id) {
        return buyRepository.findById(id)
                .map(buy -> mapToDTO(buy, new BuyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BuyDTO buyDTO) {
        final Buy buy = new Buy();
        mapToEntity(buyDTO, buy);
        return buyRepository.save(buy).getId();
    }

    public void update(final Long id, final BuyDTO buyDTO) {
        final Buy buy = buyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(buyDTO, buy);
        buyRepository.save(buy);
    }

    public void delete(final Long id) {
        buyRepository.deleteById(id);
    }

    private BuyDTO mapToDTO(final Buy buy, final BuyDTO buyDTO) {
        buyDTO.setId(buy.getId());
        buyDTO.setDate(buy.getDate());
        buyDTO.setBuyComputer(buy.getBuyComputer() == null ? null : buy.getBuyComputer().getId());
        return buyDTO;
    }

    private Buy mapToEntity(final BuyDTO buyDTO, final Buy buy) {
        buy.setDate(buyDTO.getDate());
        final Computer buyComputer = buyDTO.getBuyComputer() == null ? null : computerRepository.findById(buyDTO.getBuyComputer())
                .orElseThrow(() -> new NotFoundException("buyComputer not found"));
        buy.setBuyComputer(buyComputer);
        return buy;
    }

    public boolean buyComputerExists(final Long id) {
        return buyRepository.existsByBuyComputerId(id);
    }

}
