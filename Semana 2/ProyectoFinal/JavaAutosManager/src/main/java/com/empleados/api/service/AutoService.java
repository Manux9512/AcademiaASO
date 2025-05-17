package com.empleados.api.service;

import java.util.List;

import com.empleados.api.dto.AutoDTO;

/**
 * Service interface for auto management operations
 */
public interface AutoService {

    /**
     * Get all automoviles
     * 
     * @return a list of all automoviles
     */
    List<AutoDTO> getAllAutomoviles();

    /**
     * Get an auto by ID
     * 
     * @param id the auto ID
     * @return the auto with the specified ID
     */
    AutoDTO getAutoById(Long id);

    /**
     * Create a new auto
     * 
     * @param empleadoDTO the auto data
     * @return the created auto
     */
    AutoDTO createAuto(AutoDTO empleadoDTO);

    /**
     * Update an existing auto
     * 
     * @param id the auto ID
     * @param empleadoDTO the updated auto data
     * @return the updated auto
     */
    AutoDTO updateAuto(Long id, AutoDTO empleadoDTO);

    /**
     * Delete an auto
     * 
     * @param id the auto ID
     */
    void deleteAuto(Long id);

    /**
     * Get automoviles by department
     * 
     * @param departamento the department
     * @return a list of automoviles in the specified department
     */
    List<AutoDTO> getAutomovilesByDepartamento(String departamento);
}
