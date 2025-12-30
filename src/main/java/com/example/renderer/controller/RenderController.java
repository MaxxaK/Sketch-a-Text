package com.example.renderer.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.renderer.model.ColorMode;
import com.example.renderer.model.RenderMode;
import com.example.renderer.service.RenderService;

@RestController
@RequestMapping("/render")
public class RenderController {

    private final RenderService renderService;

    public RenderController(RenderService renderService) {
        this.renderService = renderService;
    }

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> render(
        @RequestParam("image") MultipartFile image,
        @RequestParam("text") String text,
        @RequestParam("renderMode") RenderMode renderMode,
        @RequestParam("colorMode") ColorMode colorMode
    ) {

        String contentType = image.getContentType();
        if (contentType == null ||
            !(contentType.equals(MediaType.IMAGE_PNG_VALUE)
        || contentType.equals(MediaType.IMAGE_JPEG_VALUE))) {
            throw new IllegalArgumentException("Only PNG and JPEG images are allowed");
        }

        byte[] result = renderService.render(image, text, renderMode, colorMode);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .header("Content-Disposition", "attachment; filename=\"result.png\"")
            .body(result);
    }
}

