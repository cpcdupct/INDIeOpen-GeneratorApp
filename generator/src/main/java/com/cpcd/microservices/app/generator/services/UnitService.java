package com.cpcd.microservices.app.generator.services;

import com.cpcd.microservices.app.generator.models.ClientEntity;
import com.cpcd.microservices.app.servicescommons.models.requests.LearningUnitRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.UUID;
import org.xtext.generator.Main;
import org.xtext.generator.MainEvalua;

@Component
public class UnitService {

    @Autowired
    private Environment env;

    private Logger log = LoggerFactory.getLogger(UnitService.class);

    private String FOLDER_BASE = "/tmp";

    public Boolean createUnit(ClientEntity clientEntity) {
        try {
            String outputpath = this.createDirectory(clientEntity.getSecondLevel(), clientEntity.getFirstLevel());
            String fileNameTemp = FilenameUtils.concat(FOLDER_BASE + "/" + clientEntity.getFirstLevel() + "/temp",
                    UUID.randomUUID().toString() + "." + clientEntity.getTypeModel());
            this.createFile(clientEntity.getContentDefinition(), fileNameTemp);

            if (clientEntity.getTypeModel().equals("upctforma")) {
                Main.get().runGenerator(fileNameTemp, outputpath);
            } else {
                MainEvalua.get().runGenerator(fileNameTemp, outputpath);
            }

            if (clientEntity.getUnitType() == LearningUnitRequest.UnitTypes.GAME) {
                sendUnitFTP(clientEntity, outputpath);
            } else {
                sendFileShare(outputpath, clientEntity);
            }
            deleteContent(clientEntity.getFirstLevel());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void deleteContent(String teacherid) throws IOException{
        String unitdirectory = FilenameUtils.concat(FOLDER_BASE, teacherid );
        try {
            File file = new File(unitdirectory);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
       }catch (Exception e){
            System.out.println(e.getMessage());
            throw new IOException("ERROR 408: Delete local exception");
        }
    }

    public Boolean deleteUnit(ClientEntity clientEntity){
        ClientFtpService fc = null;

        try {
            if (clientEntity.getUnitType() == LearningUnitRequest.UnitTypes.GAME) {
                fc = new ClientFtpService();
                fc.open(clientEntity);
                fc.setPassiveMode();
                fc.borrarGame(clientEntity.getFirstLevel());
            } else {
                ClientFileShareService clientFileShareService = new ClientFileShareService();
                clientFileShareService.deleteUnit(clientEntity);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }finally {
            if (clientEntity.getUnitType() == LearningUnitRequest.UnitTypes.GAME) {
                closeFTPConnection(fc);
            }
        }
    }

    private void sendUnitFTP(ClientEntity clientEntity, String outputpath)throws IOException{
        ClientFtpService fc = new ClientFtpService();
        try {

            fc.open(clientEntity);
            fc.createDirectory(clientEntity.getFirstLevel());
            fc.setPassiveMode();
            sendFilesFTP(fc, outputpath);
            fc.changeDirectory("teacher");
            String outputTeacher = FilenameUtils.concat(FOLDER_BASE, clientEntity.getFirstLevel() + "/" + clientEntity.getSecondLevel() + "/teacher");
            sendFilesFTP(fc, outputTeacher);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IOException("ERROR 407: Send file FTP exception");
        } finally {
            closeFTPConnection(fc);
        }
    }

    private void closeFTPConnection(ClientFtpService fc){
        try{
            fc.closeFTP();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void sendFilesFTP(ClientFtpService fc, String outputpath) throws IOException {
        File dir = new File(outputpath);
        Iterator<File> files = FileUtils.iterateFiles(dir, TrueFileFilter.INSTANCE, null);
        while (files.hasNext()) {
            try {
                File next = files.next();
                fc.saveFile(next);
            } catch (Exception e) {
                log.error(String.format("ERROR 407: Send file FTP exception"));
                throw new IOException("ERROR 407: Send file FTP exception");
            }
        }
    }

    private void sendFileShare(String outputpath, ClientEntity clientEntity) throws IOException{
        try {
            ClientFileShareService clientFileShareService = new ClientFileShareService();
            clientFileShareService.createDirectory(clientEntity);

            File dir = new File(outputpath);
            Iterator<File> files = FileUtils.iterateFiles(dir, TrueFileFilter.INSTANCE, null);
            while (files.hasNext()) {
                File next = files.next();
                clientFileShareService.uploadFile(clientEntity, next);
            }
        } catch (Exception e) {
            throw new IOException("ERROR 408: Send file share exception");
        }
    }

    private void createFile(String contentDefinition, String fileNameTemp) throws IOException{
        try {
            File file = new File(fileNameTemp);
            FileUtils.writeStringToFile(file, contentDefinition, Charset.forName("UTF-8"));
        }catch (IOException e){
            log.error(String.format("ERROR 406: Create local file exception %s", fileNameTemp));
            throw new IOException("ERROR 406: Create local file exception");
        }
    }

    private String createDirectory(String unitid, String teacherid) throws IOException {
        try {
            String teacherdirectory = FilenameUtils.concat(FOLDER_BASE, teacherid);
            File file = new File(teacherdirectory);
            if (!file.exists()) {
                file.mkdir();
                File dirtempo1 = new File(FOLDER_BASE + "/" + teacherid + "/temp");
                dirtempo1.mkdir();
            }
            if (!unitid.equals("preview")) {
                String unitdirectory = FilenameUtils.concat(FOLDER_BASE, teacherid + "/" + unitid);
                File unit = new File(unitdirectory); // Creo el directorio de la unidad
                if (!unit.exists()) {
                    unit.mkdir();
                }
                return unitdirectory;
            } else {
                String previewdirectory = FilenameUtils.concat(FOLDER_BASE, teacherid + "/preview");
                File dirtempo2 = new File(previewdirectory);
                dirtempo2.mkdir();
                return previewdirectory;
            }
        }catch (Exception e){
            log.error("ERROR 405: Create local directory exception");
            throw new IOException("ERROR 405: Create local directory exception");
        }
    }
}
