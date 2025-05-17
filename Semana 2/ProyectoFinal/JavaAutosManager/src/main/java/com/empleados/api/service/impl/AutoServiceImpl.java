package com.empleados.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.model.Auto;
import com.empleados.api.repository.AutoRepository;
import com.empleados.api.service.AutoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the AutoService interface
 */
@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    @Autowired
    public AutoServiceImpl(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    public List<AutoDTO> getAllAutomoviles() {
        return autoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AutoDTO getAutoById(Long id) {
        Auto auto = autoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado con id: " + id));
        return convertToDTO(auto);
    }

    @Override
    public AutoDTO createAuto(AutoDTO autoDTO) {
        try {
            // Check if email already exists
            if (autoDTO.getEmail() != null && 
                autoRepository.findByEmail(autoDTO.getEmail()).isPresent()) {
                throw new DataIntegrityViolationException("Ya existe un auto con el email: " + autoDTO.getEmail());
            }
            
            Auto auto = convertToEntity(autoDTO);
            Auto savedAuto = autoRepository.save(auto);
            return convertToDTO(savedAuto);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Error al crear el auto: " + e.getMessage());
        }
    }

    @Override
    public AutoDTO updateAuto(Long id, AutoDTO autoDTO) {
        Auto existingAuto = autoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado con id: " + id));

        // Check if the new email already exists and belongs to a different auto
        if (autoDTO.getEmail() != null && !autoDTO.getEmail().equals(existingAuto.getEmail())) {
            autoRepository.findByEmail(autoDTO.getEmail()).ifPresent(e -> {
                if (!e.getId().equals(id)) {
                    throw new DataIntegrityViolationException("Ya existe un auto con el email: " + autoDTO.getEmail());
                }
            });
        }

        // Update properties
        if (autoDTO.getNombre() != null) {
            existingAuto.setNombre(autoDTO.getNombre());
        }
        if (autoDTO.getApellido() != null) {
            existingAuto.setApellido(autoDTO.getApellido());
        }
        if (autoDTO.getMarca() != null) {
            existingAuto.setMarca(autoDTO.getMarca());
        }
        if (autoDTO.getModelo() != null) {
            existingAuto.setModelo(autoDTO.getModelo());
        }
        if (autoDTO.getAntiguedad() != null) {
            existingAuto.setAntiguedad(autoDTO.getAntiguedad());
        }
        if(autoDTO.getColor() != null) {
            existingAuto.setColor(autoDTO.getColor());
        }
        if(autoDTO.getFechaCompra() != null){
            existingAuto.setFechaCompra(autoDTO.getFechaCompra());
        }
        if (autoDTO.getEmail() != null) {
            existingAuto.setEmail(autoDTO.getEmail());
        }
        if (autoDTO.getFechaContratacion() != null) {
            existingAuto.setFechaContratacion(autoDTO.getFechaContratacion());
        }
        if (autoDTO.getCosto() != null) {
            existingAuto.setCosto(autoDTO.getCosto());
        }
        if (autoDTO.getDepartamento() != null) {
            existingAuto.setDepartamento(autoDTO.getDepartamento());
        }
    

        Auto updatedAuto = autoRepository.save(existingAuto);
        return convertToDTO(updatedAuto);
    }

    @Override
    public void deleteAuto(Long id) {
        if (!autoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Auto no encontrado con id: " + id);
        }
        autoRepository.deleteById(id);
    }

    @Override
    public List<AutoDTO> getAutomovilesByDepartamento(String departamento) {
        return autoRepository.findByDepartamento(departamento).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Entity to DTO
     */
    private AutoDTO convertToDTO(Auto auto) {
        AutoDTO autoDTO = new AutoDTO();
        BeanUtils.copyProperties(auto, autoDTO);
        return autoDTO;
    }

    /**
     * Convert DTO to Entity
     */
    private Auto convertToEntity(AutoDTO autoDTO) {
        Auto auto = new Auto();
        BeanUtils.copyProperties(autoDTO, auto);
        return auto;
    }
}
