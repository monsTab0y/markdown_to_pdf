package com.makrdown2pdf.service;

import com.makrdown2pdf.DTO.MarkdownRequestDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface MarkdownService
{
    void convertMarkdownToPdf(MarkdownRequestDTO requestDTO, HttpServletResponse response);
}
