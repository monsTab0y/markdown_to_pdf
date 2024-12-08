package com.makrdown2pdf.controller;

import com.makrdown2pdf.DTO.MarkdownRequestDTO;
import com.makrdown2pdf.service.MarkdownService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MarkdownController
{
    private static final Logger logger = LoggerFactory.getLogger(MarkdownController.class);

    @Autowired
    private MarkdownService markdownService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/convert")
    public void convertMarkdownToPdf(@ModelAttribute MarkdownRequestDTO requestDTO, HttpServletResponse response) {
        logger.info("Received request to convert markdown to PDF");
        markdownService.convertMarkdownToPdf(requestDTO, response);
    }
}
