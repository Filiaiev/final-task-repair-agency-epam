package com.filiaiev.agency.web.tag;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateLocalizationTag extends TagSupport {

    private String sqlDate;
    private String locale;

    public void setSqlDate(String sqlDate) {
        this.sqlDate = sqlDate;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int doStartTag() throws JspException {

        // Custom JSTL tag
        // Parsing SQL date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale(locale));
        Date parsed = null;
        try {
            parsed = format.parse(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Formatting parsed date to localized
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale(locale));
        String date = df.format(parsed);

        try {
            pageContext.getOut().print(date);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // returning
        return SKIP_BODY;
    }

}
