package com.empleados.api.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.service.AutoService;

import java.util.List;

/**
 * REST controller for Automovile management operations
 */
@RestController
@RequestMapping("/api/automoviles")
@Tag(name = "Auto", description = "API para la gestión de automoviless")
public class AutoController {

    private final AutoService autoService;

    @Autowired
    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los automoviles", description = "Devuelve la lista de todos los automoviles registrados")
    @ApiResponse(responseCode = "200", description = "Automoviles encontrados", 
                content = @Content(schema = @Schema(implementation = AutoDTO.class)))
    public ResponseEntity<List<AutoDTO>> getAllAutomoviles() {
        List<AutoDTO> automoviles = autoService.getAllAutomoviles();
        return ResponseEntity.ok(automoviles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un auto por ID", description = "Devuelve un auto según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Auto encontrado", 
                    content = @Content(schema = @Schema(implementation = AutoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Auto no encontrado", 
                    content = @Content)
    })
    public ResponseEntity<AutoDTO> getAutoById(@PathVariable Long id) {
        AutoDTO auto = autoService.getAutoById(id);
        return ResponseEntity.ok(auto);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo auto", description = "Crea un nuevo auto con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Auto creado correctamente",
                    content = @Content(schema = @Schema(implementation = AutoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content)
    })
    public ResponseEntity<AutoDTO> createAuto(@Valid @RequestBody AutoDTO autoDTO) {
        AutoDTO createdAuto = autoService.createAuto(autoDTO);
        return new ResponseEntity<>(createdAuto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un auto", description = "Actualiza los datos de un auto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Auto actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = AutoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Auto no encontrado", 
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", 
                    content = @Content)
    })
    public ResponseEntity<AutoDTO> updateAuto(@PathVariable Long id, @Valid @RequestBody AutoDTO autoDTO) {
        AutoDTO updatedAuto = autoService.updateAuto(id, autoDTO);
        return ResponseEntity.ok(updatedAuto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un auto", description = "Elimina un auto según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Auto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Auto no encontrado", 
                    content = @Content)
    })
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id) {
        autoService.deleteAuto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departamento/{departamento}")
    @Operation(summary = "Obtener automoviles por departamento", description = "Devuelve la lista de automoviles de un departamento específico")
    @ApiResponse(responseCode = "200", description = "Automoviles encontrados", 
                content = @Content(schema = @Schema(implementation = AutoDTO.class)))
    public ResponseEntity<List<AutoDTO>> getAutomovilesByDepartamento(@PathVariable String departamento) {
        List<AutoDTO> automoviles = autoService.getAutomovilesByDepartamento(departamento);
        return ResponseEntity.ok(automoviles);
    }
}
