package com.cpcd.microservices.app.generator.models;

import com.azure.storage.file.share.ShareFileClientBuilder;
import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;

public class ClientEntity {
    private ShareFileClientBuilder clientBuilder;
    private String typeModel;
    private String sFTP;
    private String sUser;
    private String sPassword;
    private String workingDirectory;
    private String secondLevel;
    private String firstLevel;
    private String contentDefinition;
    private LearningUnitRequest.UnitTypes unitType;

    public ShareFileClientBuilder getClientBuilder() {
        return clientBuilder;
    }

    public void setClientBuilder(ShareFileClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public String getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(String typeModel) {
        this.typeModel = typeModel;
    }

    public String getsFTP() {
        return sFTP;
    }

    public void setsFTP(String sFTP) {
        this.sFTP = sFTP;
    }

    public String getsUser() {
        return sUser;
    }

    public void setsUser(String sUser) {
        this.sUser = sUser;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(String secondLevel) {
        this.secondLevel = secondLevel;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(String firstLevel) {
        this.firstLevel = firstLevel;
    }

    public String getContentDefinition() {
        return contentDefinition;
    }

    public void setContentDefinition(String contentDefinition) {
        this.contentDefinition = contentDefinition;
    }

    public LearningUnitRequest.UnitTypes getUnitType() {
        return unitType;
    }

    public void setUnitType(LearningUnitRequest.UnitTypes unitType) {
        this.unitType = unitType;
    }
}
