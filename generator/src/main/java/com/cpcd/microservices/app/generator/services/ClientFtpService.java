package com.cpcd.microservices.app.generator.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.cpcd.microservices.app.generator.models.ClientEntity;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

public class ClientFtpService {
    private FTPSClient ftps;

    public void open(ClientEntity clientEntity) throws IOException {
        try {
            ftps = new FTPSClient();
            ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply;

            ftps.connect(clientEntity.getsFTP());

            reply = ftps.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftps.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            ftps.login(clientEntity.getsUser(), clientEntity.getsPassword());
            ftps.setFileType(FTP.BINARY_FILE_TYPE);
            ftps.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            ftps.changeWorkingDirectory(clientEntity.getWorkingDirectory());
        } catch (IOException ioe) {
            if (ftps.isConnected())
            {
                ftps.disconnect();
            }
            System.err.println("Could not connect to server.");
            ioe.printStackTrace();
            throw new IOException("Connection problem FTP");
        }
    }

    public void changeDirectory(String directory) throws IOException {
        ftps.changeWorkingDirectory(directory);
    }

    private void deleteDirectory(String path) throws  IOException{
        FTPFile[] files = ftps.listFiles(path);
        if (files.length>0){
            for (FTPFile ftpFile: files){
                if (ftpFile.isDirectory()){
                    deleteDirectory(path + "\\" + ftpFile.getName());
                }else{
                    String deleteFilePath = path + "\\" + ftpFile.getName();
                    ftps.deleteFile(deleteFilePath);
                }
            }
        }
        ftps.removeDirectory(path);
    }

    public void borrarGame(String code) throws IOException  {
        deleteDirectory(code);
    }

    public void createDirectory(String code) throws IOException  {
        boolean success;
        if (!ftps.changeWorkingDirectory(code)) {
            success = ftps.makeDirectory(code);
            if (!success) {
                throw new IOException("Error creating file");
            }
            ftps.changeWorkingDirectory(code);
            success = ftps.makeDirectory("teacher");
            if (!success) {
                throw new IOException("Error creating directory");
            }
        }
    }

    public void setPassiveMode() {
        ftps.enterLocalPassiveMode();
    }
    public void saveFile(File filetosend) throws IOException {
        FileInputStream fisv = new FileInputStream(filetosend);
        ftps.storeFile(filetosend.getName(), fisv);
        fisv.close();
    }

    public void closeFTP() throws IOException {
        ftps.logout();
        ftps.disconnect();
    }
}
