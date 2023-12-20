package com.boreckik.computershop.buy;

import com.boreckik.computershop.computer.Computer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BuyRepository extends JpaRepository<Buy, Long> {

    Buy findFirstByBuyComputer(Computer computer);

    boolean existsByBuyComputerId(Long id);

}
