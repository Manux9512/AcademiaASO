package com.empleados.api.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.service.AutoService;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para las vistas web
 */
@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final AutoService autoService;
    
    /**
     * Página de inicio (Dashboard)
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        List<AutoDTO> automoviles = autoService.getAllAutomoviles();
        
        // Calcular estadísticas
        int totalAutomoviles = automoviles.size();
        
        BigDecimal costoPromedio = automoviles.stream()
                .map(AutoDTO::getCosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, totalAutomoviles)), BigDecimal.ROUND_HALF_UP);
        
        // Distribución por departamento
        Map<String, Long> marcas = automoviles.stream()
                .collect(Collectors.groupingBy(AutoDTO::getMarca, Collectors.counting()));
        
        model.addAttribute("totalAutomoviles", totalAutomoviles);
        model.addAttribute("costoPromedio", costoPromedio.toString());
        model.addAttribute("marcas", marcas);
        
        return "index";
    }
    
    /**
     * Lista de automoviles
     */
    @GetMapping("/automoviles")
    public String listarAutomoviles(Model model) {
        model.addAttribute("automoviles", autoService.getAllAutomoviles());
        return "automoviles";
    }
    
    /**
     * Formulario para nuevo auto
     */
    @GetMapping("/automoviles/nuevo")
    public String nuevoAutoForm(Model model) {
        AutoDTO auto = new AutoDTO();
        auto.setFechaContratacion(LocalDate.now());
        model.addAttribute("auto", auto);
        return "form";
    }
    
    /**
     * Guardar nuevo auto
     */
    @PostMapping("/automoviles/guardar")
    public String guardarAuto(@Valid @ModelAttribute("auto") AutoDTO auto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "form";
        }
        
        autoService.createAuto(auto);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Auto creado correctamente!");
        return "redirect:/automoviles";
    }
    
    /**
     * Formulario para editar auto
     */
    @GetMapping("/automoviles/editar/{id}")
    public String editarAutoForm(@PathVariable Long id, Model model) {
        AutoDTO auto = autoService.getAutoById(id);
        model.addAttribute("auto", auto);
        return "form";
    }
    
    /**
     * Actualizar auto existente
     */
    @PostMapping("/automoviles/actualizar/{id}")
    public String actualizarAuto(@PathVariable Long id, 
                                    @Valid @ModelAttribute("auto") AutoDTO auto,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "form";
        }
        
        autoService.updateAuto(id, auto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Auto actualizado correctamente!");
        return "redirect:/automoviles";
    }
    
    /**
     * Eliminar auto
     */
    @PostMapping("/automoviles/eliminar/{id}")
    public String eliminarAuto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        autoService.deleteAuto(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Auto eliminado correctamente!");
        return "redirect:/automoviles";
    }
}