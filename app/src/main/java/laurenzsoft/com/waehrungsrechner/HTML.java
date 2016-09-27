package laurenzsoft.com.waehrungsrechner;

/**
 * Created by Laurenz on 24.08.2016.
 */
public class HTML {
    private static String getStyleSheet() {
        return "<style title=\"seriös\">  html { font-family: serif; } .attributeName { font-weight: bold; padding-right: 5px; } .attribute { font-weight: lighter; } .time { font-style:italic; } .bilanz { font-family: sans-serif; } .text { font-family: sans-serif; font-weight: lighter; }</style>";
    }
    private static String getHeaderStart() {
        return "<!doctype html> <html><head><meta charset=\"utf-8\"><title>Log</title>";
    }
    private static String getHeaderEnd() {
        return "</head><body>";
    }
    public static String getHeader() {
        return getHeaderStart() + getStyleSheet() + getHeaderEnd();
    }
}
