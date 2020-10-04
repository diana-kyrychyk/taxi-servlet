package ua.com.taxi.util.jsp.tag;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTag extends TagSupport {

    private LocalDateTime date;
    private String pattern;


    @Override
    public int doEndTag() {
        String formatDate = date.format(DateTimeFormatter.ofPattern(pattern));
        try {
            this.pageContext.getOut().print(formatDate);
        } catch (IOException ignored) {
        }
        return 1;
    }


    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
