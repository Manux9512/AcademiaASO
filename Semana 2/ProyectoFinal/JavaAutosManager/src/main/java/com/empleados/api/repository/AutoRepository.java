package com.empleados.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empleados.api.model.Auto;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Auto entity to handle database operations
 */
@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {
    
    /**
     * Find an auto by email
     * 
     * @param email the email to search for
     * @return an Optional containing the auto if found
     */
    Optional<Auto> findByEmail(String email);
    
    
    /**
     * Find automoviles by department
     * 
     * @param departamento the department to search for
     * @return a list of automoviles in the specified department
     */
    List<Auto> findByDepartamento(String departamento);
}
