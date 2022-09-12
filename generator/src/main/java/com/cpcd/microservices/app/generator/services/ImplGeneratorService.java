package com.cpcd.microservices.app.generator.services;

import com.cpcd.microservices.app.generator.configuration.ClientConfiguration;
import com.cpcd.microservices.app.generator.models.ClientEntity;
import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest.UnitTypes.GAME;
import static com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest.UnitTypes.PUBLIC;

@Service
public class ImplGeneratorService implements GeneratorService {
    private Logger log = LoggerFactory.getLogger(ImplGeneratorService.class);

    @Autowired
    protected UnitService unitService;

    @Autowired
    protected ClientConfiguration ClientConfiguration;

    @Override
    public Boolean createUnit(LearningUnitRequest learningUnit, String type) {
        ClientEntity clientEntity = ClientConfiguration.setClientConfiguration(learningUnit, type);
        return unitService.createUnit(clientEntity);
    }

    @Override
    public Boolean deleteUnit(String unitid, String teacherid, LearningUnitRequest.UnitTypes unitTypes) {
        ClientEntity clientEntity = ClientConfiguration.setClientConfiguration(unitid, teacherid, unitTypes);
        return unitService.deleteUnit(clientEntity);
    }
}
