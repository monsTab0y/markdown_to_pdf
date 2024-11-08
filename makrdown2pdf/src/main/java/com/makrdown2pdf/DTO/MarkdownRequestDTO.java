package com.makrdown2pdf.DTO;

import org.springframework.web.multipart.MultipartFile;

public class MarkdownRequestDTO {
    private MultipartFile file;

    public MarkdownRequestDTO(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
