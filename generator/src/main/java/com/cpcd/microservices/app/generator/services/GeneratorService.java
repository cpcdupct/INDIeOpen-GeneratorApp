package com.cpcd.microservices.app.generator.services;

import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;

public interface GeneratorService {
    public Boolean createUnit(LearningUnitRequest learningUnit, String type);

    public Boolean deleteUnit(String unitid, String teacherid, LearningUnitRequest.UnitTypes unitTypes);
    public Boolean crearPrueba();
    public Boolean borrarPrueba();
    public Boolean crearPruebaGame();
}
