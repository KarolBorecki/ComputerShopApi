package com.boreckik.computershop.buy;

import com.boreckik.computershop.computer.Computer;
import com.boreckik.computershop.computer.ComputerRepository;
import com.boreckik.computershop.util.CustomCollectors;
import com.boreckik.computershop.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/buys")
public class BuyController {

    private final BuyService buyService;
    private final ComputerRepository computerRepository;

    public BuyController(final BuyService buyService, final ComputerRepository computerRepository) {
        this.buyService = buyService;
        this.computerRepository = computerRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("buyComputerValues", computerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Computer::getId, Computer::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("buys", buyService.findAll());
        return "buy/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("buy") final BuyDTO buyDTO) {
        return "buy/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("buy") @Valid final BuyDTO buyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("buyComputer") && buyDTO.getBuyComputer() != null && buyService.buyComputerExists(buyDTO.getBuyComputer())) {
            bindingResult.rejectValue("buyComputer", "Exists.buy.buyComputer");
        }
        if (bindingResult.hasErrors()) {
            return "buy/add";
        }
        buyService.create(buyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("buy.create.success"));
        return "redirect:/buys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("buy", buyService.get(id));
        return "buy/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("buy") @Valid final BuyDTO buyDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final BuyDTO currentBuyDTO = buyService.get(id);
        if (!bindingResult.hasFieldErrors("buyComputer") && buyDTO.getBuyComputer() != null &&
                !buyDTO.getBuyComputer().equals(currentBuyDTO.getBuyComputer()) &&
                buyService.buyComputerExists(buyDTO.getBuyComputer())) {
            bindingResult.rejectValue("buyComputer", "Exists.buy.buyComputer");
        }
        if (bindingResult.hasErrors()) {
            return "buy/edit";
        }
        buyService.update(id, buyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("buy.update.success"));
        return "redirect:/buys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        buyService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("buy.delete.success"));
        return "redirect:/buys";
    }

}
