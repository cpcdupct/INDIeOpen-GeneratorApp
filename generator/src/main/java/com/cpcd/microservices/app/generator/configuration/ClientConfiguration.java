package com.cpcd.microservices.app.generator.configuration;

import com.azure.storage.file.share.ShareFileClientBuilder;
import com.cpcd.microservices.app.generator.models.ClientEntity;
import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ClientConfiguration {
    private Logger log = LoggerFactory.getLogger(ClientConfiguration.class);

    @Autowired
    private Environment env;

    private void setConfiguration(ClientEntity clientEntity){
        switch (clientEntity.getUnitType()){
            case PUBLIC:{

                clientEntity.setClientBuilder(new ShareFileClientBuilder()
                        .connectionString("MY_FILE_SHARE_CONNECTION");
                break;
            }
            case PRIVATE:{
                clientEntity.setClientBuilder(new ShareFileClientBuilder()
                        .connectionString("MY_FILE_SHARE_CONNECTION");
                break;
            }
            case GAME:{
                clientEntity.setsFTP("MY_FTP_SERVER");
                clientEntity.setsUser("MY_FTP_USER");
                clientEntity.setsPassword("MY_FTP_PASSWORD");
                clientEntity.setWorkingDirectory("MY_FTP_DIRECTORY");
                break;
            }
        }
    }

    public ClientEntity setClientConfiguration(String unitId, String teacherId, LearningUnitRequest.UnitTypes unitType) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setSecondLevel(unitId);
        clientEntity.setFirstLevel(teacherId);
        clientEntity.setUnitType(unitType);
        setConfiguration(clientEntity);
        return clientEntity;
    }
    public ClientEntity setClientConfiguration(LearningUnitRequest learningUnit, String type) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setSecondLevel(learningUnit.getUnitid());
        clientEntity.setFirstLevel(learningUnit.getTeacherid());
        clientEntity.setContentDefinition(learningUnit.getContentDefinition());
      
        clientEntity.setUnitType(learningUnit.getType());
        clientEntity.setTypeModel(type);
        setConfiguration(clientEntity);
        return clientEntity;
    }
}
