package com.empleados.api.ui.controller;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.service.AutoService;
import com.empleados.api.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WebControllerTest {

    @Mock
    private AutoService autoService;

    @InjectMocks
    private WebController webController;

    private MockMvc mockMvc;
    private AutoDTO autoDTO1;
    private AutoDTO autoDTO2;
    private List<AutoDTO> automovilesList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(webController)
                .setViewResolvers((viewName, locale) -> {
                    // Mock view resolver que solo devuelve un mock view para evitar el error
                    return mock(org.springframework.web.servlet.View.class);
                })
                .build();
        
        // Configurar datos de prueba
        autoDTO1 = TestDataBuilder.createAutoDTO();
        autoDTO2 = TestDataBuilder.createAutoDTO2();
        automovilesList = Arrays.asList(autoDTO1, autoDTO2);
    }

    @Test
    @DisplayName("Debería mostrar el dashboard")
    void dashboard_ShouldDisplayDashboard() throws Exception {
        // Arrange
        when(autoService.getAllAutomoviles()).thenReturn(automovilesList);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("totalAutomoviles"))
                .andExpect(model().attributeExists("costoPromedio"))
                .andExpect(model().attributeExists("marcas"))
                .andExpect(model().attribute("totalAutomoviles", 2));
        
        verify(autoService, times(1)).getAllAutomoviles();
        
        // Verificar el nombre de la vista directamente
        Model model = mock(Model.class);
        String viewName = webController.dashboard(model);
        assertEquals("index", viewName);
    }

    @Test
    @DisplayName("Debería listar todos los automoviles")
    void listarAutomoviles_ShouldListAllAutomoviles() throws Exception {
        // Arrange
        when(autoService.getAllAutomoviles()).thenReturn(automovilesList);

        // Act & Assert
        mockMvc.perform(get("/automoviles"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("automoviles"))
                .andExpect(model().attribute("automoviles", automovilesList));
        
        verify(autoService, times(1)).getAllAutomoviles();
        
        // Verificar el nombre de la vista directamente
        Model model = mock(Model.class);
        String viewName = webController.listarAutomoviles(model);
        assertEquals("automoviles", viewName);
    }

    @Test
    @DisplayName("Debería mostrar formulario para nuevo auto")
    void nuevoAutoForm_ShouldShowFormForNewAuto() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/automoviles/nuevo"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("auto"));
                
        // Verificar el nombre de la vista directamente
        Model model = mock(Model.class);
        String viewName = webController.nuevoAutoForm(model);
        assertEquals("form", viewName);
    }

    @Test
    @DisplayName("Debería guardar nuevo auto")
    void guardarEmpleado_WithValidData_ShouldSaveAndRedirect() throws Exception {
        // Arrange
        AutoDTO newAutoDTO = TestDataBuilder.createNewAutoDTO();
        when(autoService.createAuto(any(AutoDTO.class))).thenReturn(newAutoDTO);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Act
        String result = webController.guardarAuto(newAutoDTO, bindingResult, redirectAttributes);
        
        // Assert
        assertEquals("redirect:/automoviles", result);
        verify(autoService, times(1)).createAuto(any(AutoDTO.class));
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    @DisplayName("Debería mostrar formulario con datos del auto a editar")
    void editarEmpleadoForm_ShouldShowFormWithEmpleadoData() throws Exception {
        // Arrange
        when(autoService.getAutoById(1L)).thenReturn(autoDTO1);

        // Act & Assert
        mockMvc.perform(get("/automoviles/editar/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("auto"))
                .andExpect(model().attribute("auto", autoDTO1));
        
        verify(autoService, times(1)).getAutoById(1L);
        
        // Verificar el nombre de la vista directamente
        Model model = mock(Model.class);
        String viewName = webController.editarAutoForm(1L, model);
        assertEquals("form", viewName);
    }

    @Test
    @DisplayName("Debería actualizar auto existente")
    void actualizarEmpleado_WithValidData_ShouldUpdateAndRedirect() throws Exception {
        // Arrange
        AutoDTO updatedEmpleadoDTO = new AutoDTO(
            1L,
            "Juan Carlos",
            "Pérez",
            "Ford",
            "Mustang",
            "Negro",
            LocalDate.of(2020, 1, 15),
            "juan.perez@example.com",
            LocalDate.of(2020, 1, 15),
            new BigDecimal("55000.00"),
            "Tecnología",
            1
        );
        
        when(autoService.updateAuto(eq(1L), any(AutoDTO.class))).thenReturn(updatedEmpleadoDTO);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Act
        String result = webController.actualizarAuto(1L, updatedEmpleadoDTO, bindingResult, redirectAttributes);
        
        // Assert
        assertEquals("redirect:/automoviles", result);
        verify(autoService, times(1)).updateAuto(eq(1L), any(AutoDTO.class));
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    @DisplayName("Debería eliminar auto existente")
    void eliminarEmpleado_ShouldDeleteAndRedirect() throws Exception {
        // Arrange
        doNothing().when(autoService).deleteAuto(1L);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Act
        String result = webController.eliminarAuto(1L, redirectAttributes);
        
        // Assert
        assertEquals("redirect:/automoviles", result);
        verify(autoService, times(1)).deleteAuto(1L);
        verify(redirectAttributes).addFlashAttribute(eq("mensajeExito"), anyString());
    }

    @Test
    @DisplayName("Debería retornar al formulario cuando hay errores de validación al guardar")
    void guardarEmpleado_WithValidationErrors_ShouldReturnToForm() {
        // Arrange
        AutoDTO invalidAutoDTO = TestDataBuilder.createInvalidAutoDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Act
        String viewName = webController.guardarAuto(invalidAutoDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("form", viewName);
        verify(autoService, times(0)).createAuto(any(AutoDTO.class));
    }

    @Test
    @DisplayName("Debería retornar al formulario cuando hay errores de validación al actualizar")
    void actualizarEmpleado_WithValidationErrors_ShouldReturnToForm() {
        // Arrange
        AutoDTO invalidAutoDTO = TestDataBuilder.createInvalidAutoDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Act
        String viewName = webController.actualizarAuto(1L, invalidAutoDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("form", viewName);
        verify(autoService, times(0)).updateAuto(anyLong(), any(AutoDTO.class));
    }

    @Test
    @DisplayName("Debería calcular correctamente las estadísticas del dashboard")
    void dashboard_ShouldCalculateStatisticsCorrectly() {
        // Arrange
        when(autoService.getAllAutomoviles()).thenReturn(automovilesList);
        Model model = mock(Model.class);

        // Act
        String viewName = webController.dashboard(model);

        // Assert
        assertEquals("index", viewName);
        verify(model).addAttribute(eq("totalAutomoviles"), eq(2));
        verify(model).addAttribute(eq("costoPromedio"), anyString());
        verify(model).addAttribute(eq("marcas"), any());
        verify(autoService, times(1)).getAllAutomoviles();
    }
}