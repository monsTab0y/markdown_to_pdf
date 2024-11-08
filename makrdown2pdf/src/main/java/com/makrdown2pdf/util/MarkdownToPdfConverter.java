package com.makrdown2pdf.util;

import java.io.OutputStream;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class MarkdownToPdfConverter {

    public static void generatePdfFromHtml(String html, OutputStream outputStream) {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null); // null here means no base URI, or use a base URI if you have one
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
