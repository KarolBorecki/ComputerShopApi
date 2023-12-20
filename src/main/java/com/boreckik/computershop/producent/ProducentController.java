package com.boreckik.computershop.producent;

import com.boreckik.computershop.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/producents")
public class ProducentController {

    private final ProducentService producentService;

    public ProducentController(final ProducentService producentService) {
        this.producentService = producentService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("producents", producentService.findAll());
        return "producent/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("producent") final ProducentDTO producentDTO) {
        return "producent/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("producent") @Valid final ProducentDTO producentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "producent/add";
        }
        producentService.create(producentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("producent.create.success"));
        return "redirect:/producents";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("producent", producentService.get(id));
        return "producent/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("producent") @Valid final ProducentDTO producentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "producent/edit";
        }
        producentService.update(id, producentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("producent.update.success"));
        return "redirect:/producents";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = producentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            producentService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("producent.delete.success"));
        }
        return "redirect:/producents";
    }

}
