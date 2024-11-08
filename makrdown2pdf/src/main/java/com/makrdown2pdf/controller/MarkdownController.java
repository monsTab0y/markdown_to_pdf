package com.makrdown2pdf.controller;

import com.makrdown2pdf.DTO.MarkdownRequestDTO;
import com.makrdown2pdf.service.MarkdownService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MarkdownController
{
    @Autowired
    private MarkdownService markdownService;

    @PostMapping("/convert")
    public void convertMarkdownToPdf(@ModelAttribute MarkdownRequestDTO requestDTO, HttpServletResponse response) {
        markdownService.convertMarkdownToPdf(requestDTO, response);
    }
}
