package com.makrdown2pdf.service;

import com.makrdown2pdf.DTO.MarkdownRequestDTO;
import com.makrdown2pdf.util.MarkdownToPdfConverter;
import jakarta.servlet.http.HttpServletResponse;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.commonmark.parser.Parser;
import java.io.IOException;


@Service
public class MarkdownServiceImpl implements MarkdownService{

    private static final Logger logger =  LoggerFactory.getLogger(MarkdownServiceImpl.class);
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB
    @Override
    public void convertMarkdownToPdf(MarkdownRequestDTO requestDTO, HttpServletResponse response) {
        try {
            // Validate file size
            if (requestDTO.getFile().getSize() > MAX_FILE_SIZE) {
                logger.error("File size exceeds the limit of 2 MB");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File size exceeds the limit of 2 MB");
                return;
            }
            // Validate file type
            String contentType = requestDTO.getFile().getContentType();
            if (!"text/markdown".equals(contentType) && !requestDTO.getFile().getOriginalFilename().endsWith(".md")) {
                logger.error("Invalid file type: {}", contentType);
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Only Markdown files are supported");
                return;
            }

            // Read the Markdown file
            String markdown = new String(requestDTO.getFile().getBytes());
            logger.info("Received markdown file with size: {} bytes", requestDTO.getFile().getSize());


            // Convert Markdown to HTML
            String html = convertMarkdownToHtml(markdown);

            // Set response headers for PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"converted.pdf\"");

            // Generate PDF from HTML and write to response
            MarkdownToPdfConverter.generatePdfFromHtml(html, response.getOutputStream());
            response.getOutputStream().flush();
            logger.info("PDF generated successfully and sent to client");

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error processing the file", e);

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
