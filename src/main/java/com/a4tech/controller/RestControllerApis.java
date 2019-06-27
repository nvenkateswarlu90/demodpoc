package com.a4tech.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerApis {


	@GetMapping(value = "/download/{type}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("type") String type, HttpServletRequest request) {
		// Load file as Resource
		String fileName = "D:\\downloads\\reportid.pdf";
		Resource resource = null;
		if ("pdf".equals(type)) {
			resource = loadFileAsResource(fileName);
		}
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		try {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resource.getFilename())
					.contentType(MediaType.APPLICATION_PDF).contentLength(resource.contentLength()).body(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		/*return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);*/
	}
	
	@RequestMapping(path = "/download123", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download() throws IOException {
        File file = new File("D:\\downloads\\reportid.pdf");

        HttpHeaders header = new HttpHeaders();
        Resource resource = null;
			resource = loadFileAsResource("D:\\downloads\\Popuppage.pdf");
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ resource.getFilename());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        //Path path = Paths.get(file.getAbsolutePath());
        //ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new FileInputStream(file)));
    }


	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = Paths.get(fileName);
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				// throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			// throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
		return null;
	}

}
