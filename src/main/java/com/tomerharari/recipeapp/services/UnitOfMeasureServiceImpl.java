package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.UnitOfMeasureCommand;
import com.tomerharari.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.tomerharari.recipeapp.model.UnitOfMeasure;
import com.tomerharari.recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.uomConverter = uomConverter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll()
                .spliterator(), false).
                map(uomConverter::convert).
                collect(Collectors.toSet());
    }
}
