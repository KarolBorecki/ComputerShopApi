package com.boreckik.computershop.cpu;

import com.boreckik.computershop.producent.Producent;
import com.boreckik.computershop.producent.ProducentRepository;
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
@RequestMapping("/cpus")
public class CpuController {

    private final CpuService cpuService;
    private final ProducentRepository producentRepository;

    public CpuController(final CpuService cpuService,
            final ProducentRepository producentRepository) {
        this.cpuService = cpuService;
        this.producentRepository = producentRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("cpusProducentValues", producentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Producent::getId, Producent::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cpus", cpuService.findAll());
        return "cpu/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cpu") final CpuDTO cpuDTO) {
        return "cpu/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cpu") @Valid final CpuDTO cpuDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cpu/add";
        }
        cpuService.create(cpuDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cpu.create.success"));
        return "redirect:/cpus";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("cpu", cpuService.get(id));
        return "cpu/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("cpu") @Valid final CpuDTO cpuDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cpu/edit";
        }
        cpuService.update(id, cpuDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cpu.update.success"));
        return "redirect:/cpus";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cpuService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cpuService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cpu.delete.success"));
        }
        return "redirect:/cpus";
    }

}
