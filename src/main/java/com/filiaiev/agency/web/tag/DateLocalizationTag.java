package com.filiaiev.agency.web.tag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateLocalizationTag extends TagSupport {

    private static Logger logger = Logger.getLogger(DateLocalizationTag.class);

    private String sqlDate;
    private String locale;

    public void setSqlDate(String sqlDate) {
        this.sqlDate = sqlDate;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int doStartTag() {
        // Custom JSTL Tag, parsing SQL datetime
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale(locale));
        Date parsed = null;
        try {
            parsed = format.parse(sqlDate);
        } catch (ParseException e) {
            logger.warn("Cannot parse SQL date due to --> " + e.getMessage(), e);
        }

        // Formatting parsed date to localized
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale(locale));
        String date = df.format(parsed);

        try {
            pageContext.getOut().print(date);
        } catch (IOException e) {
            logger.warn("Cannot out parsed date to page due to --> " + e.getMessage(), e);
        }
        return SKIP_BODY;
    }

}
