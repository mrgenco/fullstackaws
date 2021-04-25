package com.mrg.aws.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File convertMultiPartFileToFile(final MultipartFile multipartFile) throws Exception {
        final File file = new File(multipartFile.getOriginalFilename());
        try(final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            System.out.println("IO exception occurred");
            throw ex;
        }
        return file;
    }

    public static void deleteIfExist(File file){
        if(file!=null){
            boolean isDeleted = file.delete();
            if(isDeleted) System.out.print("File is deleted");
            else System.out.print("File cannot be deleted");
        }
    }

    public static boolean isFileNotEmpty(final MultipartFile file) {
        if (file==null || file.isEmpty() ) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new IllegalStateException("File name cannot be empty");
        }
        return true;
    }
}
