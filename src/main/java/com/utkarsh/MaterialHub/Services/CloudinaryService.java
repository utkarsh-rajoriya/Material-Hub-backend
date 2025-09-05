package com.utkarsh.MaterialHub.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadNotes(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "type", "upload",
                        "folder", "Material-Hub",
                        "use_filename", true,
                        "unique_filename", true
                )
        );
        return uploadResult.get("secure_url").toString();
    }

    public String dltNote(String fileUrl) {
        try {
            String publicId = extractPublicId(fileUrl);

            // Always delete from "image" bucket (since auto puts PDFs there too)
            Map<?, ?> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );

            return result.get("result").toString(); // "ok" or "not found"
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from Cloudinary: " + e.getMessage(), e);
        }
    }


    private String extractPublicId(String fileUrl) {
        // Example: https://res.cloudinary.com/demo/raw/upload/v1693292376/Material-Hub/myfile.pdf

        String withoutUpload = fileUrl.substring(fileUrl.indexOf("/upload/") + 8);
        // -> v1693292376/Material-Hub/myfile.pdf

        // Remove version (starts with "v" + digits)
        if (withoutUpload.matches("^v[0-9]+/.*")) {
            withoutUpload = withoutUpload.substring(withoutUpload.indexOf("/") + 1);
        }

        // Remove extension
        int dotIndex = withoutUpload.lastIndexOf(".");
        if (dotIndex != -1) {
            withoutUpload = withoutUpload.substring(0, dotIndex);
        }

        return withoutUpload; // -> "Material-Hub/myfile"
    }
}