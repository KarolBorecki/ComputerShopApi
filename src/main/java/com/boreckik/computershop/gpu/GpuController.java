package com.boreckik.computershop.gpu;

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
@RequestMapping("/gpus")
public class GpuController {

    private final GpuService gpuService;
    private final ProducentRepository producentRepository;

    public GpuController(final GpuService gpuService,
            final ProducentRepository producentRepository) {
        this.gpuService = gpuService;
        this.producentRepository = producentRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("gpusProducentValues", producentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Producent::getId, Producent::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("gpus", gpuService.findAll());
        return "gpu/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("gpu") final GpuDTO gpuDTO) {
        return "gpu/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("gpu") @Valid final GpuDTO gpuDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "gpu/add";
        }
        gpuService.create(gpuDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("gpu.create.success"));
        return "redirect:/gpus";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("gpu", gpuService.get(id));
        return "gpu/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("gpu") @Valid final GpuDTO gpuDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "gpu/edit";
        }
        gpuService.update(id, gpuDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("gpu.update.success"));
        return "redirect:/gpus";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = gpuService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            gpuService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("gpu.delete.success"));
        }
        return "redirect:/gpus";
    }

}
