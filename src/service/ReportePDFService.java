package service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio de generacion de reportes en PDF.
 * <p>
 * Toma los datos de un reporte (List&lt;Map&lt;String,Object&gt;&gt;)
 * y genera un archivo PDF con una tabla formateada y un encabezado
 * con el nombre del reporte.
 */
public class ReportePDFService {

    private final Document document;
    private final String nombreReporte;

    public ReportePDFService(String nombreReporte) {
        this.nombreReporte = nombreReporte;
        // Cabecera A4
        document = new Document(PageSize.A4);
    }

    /**
     * Genera el archivo PDF en la ruta indicada.
     *
     * @param datos       Lista de mapas con los resultados del reporte.
     * @param rutaSalida  Ruta donde guardar el archivo PDF.
     * @throws DocumentException Si hay error al crear el PDF.
     * @throws IOException       Si no se puede escribir el archivo.
     */
    public void generar(List<Map<String, Object>> datos, String rutaSalida)
            throws DocumentException, IOException {

        PdfWriter.getInstance(document, new FileOutputStream(rutaSalida));
        document.open();

        // Encabezado con el nombre del reporte
        addEncabezado();

        if (datos == null || datos.isEmpty()) {
            addMensajeVacio();
        } else {
            // Obtener las columnas del primer registro
            String[] columnas = obtenerClaves(datos.get(0));
            addTabla(datos, columnas);
        }

        document.close();
    }

    private void addEncabezado() throws DocumentException {
        document.add(new Paragraph("QuindíoStay - Reportes"));
        document.add(new Paragraph("Generado: " + LocalDateTime.now()));
        document.add(new Paragraph("Reporte: " + nombreReporte));
        document.add(new Paragraph("----------------------------------------"));
        document.add(new Paragraph(""));
    }

    private void addMensajeVacio() throws DocumentException {
        document.add(new Paragraph("No hay datos para este reporte."));
        document.add(new Paragraph("Verifica que la base de datos tenga los datos necesarios."));
    }

    private String[] obtenerClaves(Map<String, Object> primerRegistro) {
        return primerRegistro.keySet().toArray(new String[0]);
    }

    private void addTabla(List<Map<String, Object>> datos, String[] columnas)
            throws DocumentException {
        int nroColumnas = columnas.length;

        // Ancho de la celda: 100% dividido entre columnas
        float[] anchos = new float[nroColumnas];
        for (int i = 0; i < nroColumnas; i++) {
            anchos[i] = 100f / nroColumnas;
        }

        PdfPTable table = new PdfPTable(nroColumnas);
        table.setWidths(anchos);
        table.setWidthPercentage(100f);

        // Cabecera de la tabla
        for (String columna : columnas) {
            table.addCell(new Phrase(columna));
        }

        // Datos
        for (Map<String, Object> fila : datos) {
            for (String columna : columnas) {
                Object valor = fila.get(columna);
                if (valor == null) {
                    valor = "";
                }
                table.addCell(new Phrase(String.valueOf(valor)));
            }
        }

        // Estilo de la tabla
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setHeaderRows(1);

        // Aplicar fondo y fuente a la cabecera
        PdfPCell[] headerCells = table.getRow(0).getCells();
        for (int i = 0; i < headerCells.length; i++) {
            headerCells[i].setBackgroundColor(
                    new com.lowagie.text.pdf.RGBColor(0, 102, 153));
            String texto = headerCells[i].getPhrase().getContent();
            Paragraph p = new Paragraph(texto);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setFont(new Font(Font.NORMAL, 9, Font.BOLD,
                    com.lowagie.text.pdf.RGBColor.WHITE));
            headerCells[i].setPhrase(p);
        }

        document.add(table);
    }
}
