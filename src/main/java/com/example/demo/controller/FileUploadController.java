package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file-upload")
public class FileUploadController {
	public static final int BUF_SIZE = 2 * 1024;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String saveOrder(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename) throws IOException {
        System.out.println("Uploading file: " + file.getName());
        System.out.println("DEstination file name: " + filename);
		if (!file.isEmpty()) {
            try {
                saveUploadFile(file.getInputStream(), new File(filename));
                return "You successfully uploaded " + filename + " into " + filename;
            } catch (Exception e) {
                return "You failed to upload " + filename + " => " + e.getMessage();
            }
        } else {
        	System.out.println("You failed to upload " + filename + " because the file was empty.");
            return "You failed to upload " + filename + " because the file was empty.";
        }
	}
	
	private void saveUploadFile(InputStream input, File dst) throws IOException {
        OutputStream out = null;
        try {
            if (dst.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(dst, true),
                        BUF_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        BUF_SIZE);
            }
            byte[] buffer = new byte[BUF_SIZE];
            int len = 0;
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public String getStatus() {
		return "File upload microservice is up";
	}
}
