import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Order;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.geometry.HorizontalDirection;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Test {

    private static Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, "windows-1251", 16F);
    private static final String FONT_NAME = "fonts/arial.ttf";

    public static void main(String[] args) throws ParseException, IOException, DocumentException, URISyntaxException {
        Document document = new Document();
//        PrintStream ps = new PrintStream(new FileOutputStream("pdf-test.pdf"), true, "windows-1251");
//        PdfWriter pdfWriter = PdfWriter.getInstance(document, ps);
        BaseFont bf = BaseFont.createFont(FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        document.open();
        Order order = new Order();
        order.setId(1);
        order.setOrderDate(Timestamp.valueOf("2021-06-09 18:53:30"));
        order.setStatusName("Виконано");
        order.setCost(BigDecimal.valueOf(500));

        for (int i = 0; i < 3; i++) {
            PdfPTable table = new PdfPTable(2);
            table.setSpacingAfter(50);
            addTableHeader(table, order, new Font(bf, 14, Font.NORMAL));
            addRows(table, order, new Font(bf, 14, Font.NORMAL));
//            addCustomRows(table);

            document.add(table);
        }
        document.close();
    }

    private static void addTableHeader(PdfPTable table, Order order, Font font) {
        Stream.of("Замовлення №" + order.getId())
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.CYAN);
                    header.setBorderWidth(0.5F);
                    header.setColspan(2);
                    header.setPhrase(new Phrase(columnTitle, font));
                    header.setPadding(5);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table, Order order, Font font) {
        Phrase phrase = new Phrase();
        phrase.setFont(font);
        phrase.add("Дата замовлення");
        table.addCell(phrase);
        table.addCell(order.getOrderDate().toString());
        table.addCell("Ціна");
        table.addCell(order.getCost().toString());
        table.addCell("Статус");
        table.addCell(order.getStatusName());
    }

//    private static void addCustomRows(PdfPTable table)
//            throws URISyntaxException, BadElementException, IOException {
//        Phrase phrase = new Phrase();
//        phrase.add("Phrase");
//        PdfPCell imageCell = new PdfPCell(phrase);
//        table.addCell(imageCell);
//
//        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
//        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(horizontalAlignCell);
//
//        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
//        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//        table.addCell(verticalAlignCell);
//    }
}
