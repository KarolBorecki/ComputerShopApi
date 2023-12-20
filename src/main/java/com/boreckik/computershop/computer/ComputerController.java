package com.boreckik.computershop.computer;

import com.boreckik.computershop.cpu.Cpu;
import com.boreckik.computershop.cpu.CpuRepository;
import com.boreckik.computershop.gpu.Gpu;
import com.boreckik.computershop.gpu.GpuRepository;
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
@RequestMapping("/computers")
public class ComputerController {

    private final ComputerService computerService;
    private final CpuRepository cpuRepository;
    private final GpuRepository gpuRepository;

    public ComputerController(final ComputerService computerService,
            final CpuRepository cpuRepository, final GpuRepository gpuRepository) {
        this.computerService = computerService;
        this.cpuRepository = cpuRepository;
        this.gpuRepository = gpuRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("computersCpuValues", cpuRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cpu::getId, Cpu::getName)));
        model.addAttribute("computersGpusValues", gpuRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Gpu::getId, Gpu::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("computers", computerService.findAll());
        return "computer/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("computer") final ComputerDTO computerDTO) {
        return "computer/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("computer") @Valid final ComputerDTO computerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "computer/add";
        }
        computerService.create(computerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("computer.create.success"));
        return "redirect:/computers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("computer", computerService.get(id));
        return "computer/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("computer") @Valid final ComputerDTO computerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "computer/edit";
        }
        computerService.update(id, computerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("computer.update.success"));
        return "redirect:/computers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = computerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            computerService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("computer.delete.success"));
        }
        return "redirect:/computers";
    }

}
