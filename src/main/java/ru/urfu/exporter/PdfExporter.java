package ru.urfu.exporter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ru.urfu.document.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Экспортёр документов в формат PDF.
 */
@Component
public class PdfExporter implements DocumentExporter {

    @Override
    public void export(Document document, Path outputPath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            com.itextpdf.text.Document pdf = new com.itextpdf.text.Document();
            PdfWriter.getInstance(pdf, outputStream);

            pdf.open();
            pdf.add(new Paragraph(document.content()));
            pdf.close();
        } catch (DocumentException e) {
            throw new IOException("Ошибка при создании PDF документа", e);
        }
    }

    @Override
    public String getFormat() {
        return "pdf";
    }
}

