package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.UnitOfMeasureCommand;
import com.tomerharari.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.tomerharari.recipeapp.model.UnitOfMeasure;
import com.tomerharari.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {
    UnitOfMeasureService uomService;
    UnitOfMeasureToUnitOfMeasureCommand uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
    @Mock
    UnitOfMeasureRepository uomRepository;

    @BeforeEach
    void setUp() {
        uomService = new UnitOfMeasureServiceImpl(uomRepository, uomConverter);

    }

    @Test
    void listAllUoms() {
        // given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        unitOfMeasures.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        unitOfMeasures.add(uom2);

        when(uomRepository.findAll()).thenReturn(unitOfMeasures);

        // when
        Set<UnitOfMeasureCommand> uoms = uomService.listAllUoms();

        // then
        assertEquals(2, uoms.size());
        verify(uomRepository).findAll();
    }
}