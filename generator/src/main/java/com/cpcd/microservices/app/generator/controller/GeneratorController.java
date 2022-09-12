package com.cpcd.microservices.app.generator.controller;

import com.cpcd.microservices.app.generator.services.GeneratorService;
import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GeneratorController {
    @Autowired
    private GeneratorService generatorService;

    @PostMapping("/contentgenerator")
    public ResponseEntity<?> createContent(@Valid @RequestBody LearningUnitRequest learningUnit, BindingResult result){
        if (result.hasErrors()) {
            return validar(result);
        }
        Boolean resultContent = generatorService.createUnit(learningUnit, "upctforma");
        if (resultContent) {
            return ResponseEntity.status(HttpStatus.CREATED).body(learningUnit);
        }else{
            return ResponseEntity.badRequest().body("");
        }

    }

    @PostMapping("/gamificationgenerator")
    public ResponseEntity<?> createGamificationContent(@Valid @RequestBody LearningUnitRequest learningUnit, BindingResult result){
        if (result.hasErrors()) {
            return validar(result);
        }
        Boolean resultContent = generatorService.createUnit(learningUnit, "upctforma");
        if (resultContent) {
            return ResponseEntity.status(HttpStatus.CREATED).body(learningUnit);
        }else{
            return ResponseEntity.badRequest().body("");
        }

    }

    @DeleteMapping("/learninggenerator/{unitid}/{teacherid}/{unitTypes}")
    public ResponseEntity<?> borrarUnidad(@PathVariable String unitid, @PathVariable String teacherid,
                                          @PathVariable LearningUnitRequest.UnitTypes unitTypes){

        if (unitid == null || teacherid == null|| unitTypes == null) {
            ResponseEntity.badRequest().body("ERROR: Faltan parametros en la petición");
        }
        Boolean result = generatorService.deleteUnit(unitid,teacherid, unitTypes);
        if (result) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/evaluationgenerator")
    public ResponseEntity<?> createEvaluation(@Valid @RequestBody LearningUnitRequest learningUnit, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Boolean resultEvaluation = generatorService.createUnit(learningUnit, "upctformaevalua");
        if (resultEvaluation) {
            return ResponseEntity.status(HttpStatus.CREATED).body(learningUnit);
        }else{
            return ResponseEntity.badRequest().body("");
        }
    }

    protected ResponseEntity<?> validar(BindingResult result){
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errores.put(err.getField(),  "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
