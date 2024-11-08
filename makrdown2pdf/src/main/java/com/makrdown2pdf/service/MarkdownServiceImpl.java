package com.makrdown2pdf.service;

import com.makrdown2pdf.DTO.MarkdownRequestDTO;
import com.makrdown2pdf.util.MarkdownToPdfConverter;
import jakarta.servlet.http.HttpServletResponse;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.commonmark.parser.Parser;
import java.io.IOException;


@Service
public class MarkdownServiceImpl implements MarkdownService{
    @Override
    public void convertMarkdownToPdf(MarkdownRequestDTO requestDTO, HttpServletResponse response) {
        try {
            // Read the Markdown file
            String markdown = new String(requestDTO.getFile().getBytes());

            // Convert Markdown to HTML
            String html = convertMarkdownToHtml(markdown);

            // Set response headers for PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"converted.pdf\"");

            // Generate PDF from HTML and write to response
            MarkdownToPdfConverter.generatePdfFromHtml(html, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String convertMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        org.commonmark.node.Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // Wrap content in HTML and Body tags for well-formedness
        String htmlContent = "<html><body>" + renderer.render(document) + "</body></html>";
        return htmlContent;
    }

}
