package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.order.GetOrdersCommand;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

// Servlet whose task is to generate and send report to the manager
public class GenerateReportCommand implements Command {

    private static Logger logger = Logger.getLogger(GenerateReportCommand.class);

    private static ResourceBundle rb;
    private static String FONT_NAME;
    private static String LOCALE;
    private static String report_path = "../reports/";

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Creating report .pdf file");

        // Generating path according to manager login
        User user = (User) req.getSession().getAttribute("user");
        report_path += "report-" + user.getLogin() + ".pdf";

        // Loading font to be able to write cyrillic
        FONT_NAME = req.getServletContext().getRealPath("fonts/arial.ttf");
        LOCALE = (String) req.getSession().getAttribute("lang");

        // Loading ResourceBundle to localize table text
        rb = ResourceBundle.getBundle("i18n.app", new Locale(LOCALE));

        // Getting filters from the request to generate report according to this filters
        boolean resetFilter = Boolean.parseBoolean(req.getParameter("resetFilter"));
        Map<String, String> filters = GetOrdersCommand.getFilters(req, resetFilter);
        String sortBy = req.getParameter("sortBy");
        String ordering = req.getParameter("ordering");

        List<Order> orders = new OrderDAO(LOCALE).getOrders(filters, sortBy, ordering, 0, 0);

        try{
            pdfGenerator.generate(orders);

            // Getting streams to read and write
            FileInputStream fis = new FileInputStream(report_path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            OutputStream os = resp.getOutputStream();

             /*
              *  Setting response content and header + name of the file
              *  Naming format is: 'Report - ' + localized current date and time
             */
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=\""
                    + "Report - "
                    + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date())
                    + ".pdf" + "\"");

            logger.trace("Reading " + bis.available() + " bytes");

            // reading file per 8192 bytes
            byte[] buffer = new byte[8192];
            while(bis.read(buffer) != -1){
                os.write(buffer);
                os.flush();
            }

            fis.close();
            bis.close();
            os.close();
        }catch (ParseException | DocumentException | URISyntaxException e) {
            logger.warn("Cannot create report due to --> " + e.getMessage(), e);
            return null;
        }

        // Clearing generated file
        new File(report_path).delete();
        logger.trace("Report has been successfully sent to manager");
        return null;
    }

    private static class pdfGenerator{

        /**
         * Generating .pdf file orders report
         *
         * @param orders list
        */
        static void generate(List<Order> orders) throws ParseException, IOException, DocumentException, URISyntaxException {
            Document document = new Document();
            PdfWriter.getInstance(document, new PrintStream(new FileOutputStream(report_path), true, "windows-1251"));

            // Setting basefont instantiated to arial.ttf
            BaseFont bf = BaseFont.createFont(FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 14, Font.NORMAL);
            document.open();

            // Looping through orders and writing them to file
            for (Order o : orders){
                PdfPTable table = new PdfPTable(2);
                table.setSpacingAfter(50);
                addTableHeader(table, o, font);
                addRows(table, o, font);
                document.add(table);
            }
            document.close();
        }

        // Given order and table instance creating table header
        static void addTableHeader(PdfPTable table, Order order, Font font) {
            PdfPCell header = new PdfPCell();

            // Setting header params
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(0.5F);
            header.setColspan(2);
            header.setPhrase(new Phrase(rb.getString("order.order_number")
                    + order.getId(), font));
            header.setPadding(5);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        }

        // Given table and order creating table filling
        static void addRows(PdfPTable table, Order order, Font font) {
            table.addCell(new Phrase(rb.getString("order.order_date"), font));
            table.addCell(order.getOrderDate().toString());

            table.addCell(new Phrase(rb.getString("order.order_cost"), font));
            table.addCell(Objects.toString(order.getCost(), "-"));

            table.addCell(new Phrase(rb.getString("order.order_status"), font));
            table.addCell(new Phrase(order.getStatusName(), font));
        }
    }
}
