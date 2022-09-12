package com.cpcd.microservices.app.generator.services;

import com.azure.storage.file.share.*;
import com.cpcd.microservices.app.generator.models.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class ClientFileShareService {
    public void createDirectory(ClientEntity clientEntity) throws Exception
    {
        try
        {
            String unitid = clientEntity.getSecondLevel();
            String teacherid = clientEntity.getFirstLevel();
            ShareFileClientBuilder clientBuilder = clientEntity.getClientBuilder();
            ShareDirectoryClient dirClient = clientBuilder
                    .resourcePath(teacherid)
                    .buildDirectoryClient();

            if (!dirClient.exists()) {
                dirClient.create();
                dirClient.createSubdirectory("preview");
            }
            dirClient.createSubdirectoryIfNotExists(unitid);
        }
        catch (Exception e)
        {
            System.out.println("createDirectory exception: " + e.getMessage());
            throw new Exception("ERROR 401: Create directory exception");
        }
    }

    public void deleteUnit(ClientEntity clientEntity) throws Exception
    {
        try
        {
            String unitid = clientEntity.getSecondLevel();
            String teacherid = clientEntity.getFirstLevel();
            String dirName = teacherid + "/" + unitid;
            ShareFileClientBuilder clientBuilder = clientEntity.getClientBuilder();
            ShareDirectoryClient dirClient = clientBuilder
                    .resourcePath(dirName)
                    .buildDirectoryClient();
            if (dirClient.exists()) {
                System.out.println("Borrar Directorio FileShare "  + dirName);
                deleteDirectory(dirClient);
            }
        }
        catch (Exception e)
        {
            System.out.println("deleteDirectory exception: " + e.getMessage());
            throw new Exception("ERROR 402: Delete unit exception");
        }
    }

    public void deleteDirectory(ShareDirectoryClient dirClient) throws Exception
    {
        try
        {
            dirClient.listFilesAndDirectories().forEach(
                    fileRef -> {
                        if (!fileRef.isDirectory()){
                            System.out.println("Delete File FileShare "  + fileRef.getName());
                            ShareFileClient fileClient = dirClient.getFileClient(fileRef.getName());
                            fileClient.delete();
                        }else{
                            System.out.println("Delete Directory FileShare "  + fileRef.getName());
                            ShareDirectoryClient subDir = dirClient.getSubdirectoryClient(fileRef.getName());
                            try {
                                deleteDirectory(subDir);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            subDir.delete();
                        }
                    }
            );
            dirClient.delete();
        }
        catch (Exception e)
        {
            System.out.println("Delete exception: " + e.getMessage());
            throw new Exception("ERROR 403: Delete directory exception");
        }
    }

    public void uploadFile(ClientEntity clientEntity, File file) throws Exception
    {
        try
        {
            String unitid = clientEntity.getSecondLevel();
            String teacherid = clientEntity.getFirstLevel();
            String dirName = teacherid + "/" + unitid;
            ShareFileClientBuilder clientBuilder = clientEntity.getClientBuilder();
            ShareDirectoryClient dirClient = clientBuilder
                    .resourcePath(dirName)
                    .buildDirectoryClient();
            ShareFileClient fileClient = dirClient.getFileClient(file.getName());
            fileClient.create(file.length());
            fileClient.uploadFromFile(file.getPath());
        }
        catch (Exception e)
        {
            System.out.println("uploadFile exception: " + e.getMessage());
            throw new Exception("ERROR 404: Upload file exception");
        }
    }
}

