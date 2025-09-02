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
                        // This is the most important parameter.
                        // 'auto' tells Cloudinary to correctly identify PDFs as 'raw' and JPGs/PNGs as 'image'.
                        "resource_type", "auto",

                        // 'upload' ensures the asset is public.
                        "type", "upload",

                        "folder", "Material-Hub",
                        "use_filename", true,
                        "unique_filename", true
                )
        );

        return uploadResult.get("secure_url").toString();
    }
}

