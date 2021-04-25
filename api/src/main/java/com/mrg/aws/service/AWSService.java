package com.mrg.aws.service;

import com.mrg.aws.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AWSService {
	void uploadImage(MultipartFile multipartFile, String description, String tags) throws Exception;
	byte[] downloadImage(UUID imageId);
	List<Image> findAll();
	List<Image> findBySearchTerm(String searchTerm);
}
