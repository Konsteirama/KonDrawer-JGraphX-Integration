package teo.isgci.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a svg file and converts all raw latex strings into svg-readable
 * and displayable text.
 * 
 * TODO: static >= object?
 */
public class SVGLatexConverter {
    
    /**
     * Which latexmodes are currently active. The "most current" one is always
     * the last entry, so for example co(x_{abc^3}) : CO -> SUB -> SUPER
     */
    private List<LatexMode> currentModes 
        = new ArrayList<LatexMode>(LatexMode.values().length);
    
    /** The contents of the svg file. */
    private String svgContent;
    
    /**
     * Creates a new latexconverter that converts all latexstring in the svg 
     * file on the given path to suitable, svg-drawable graphics.
     * @param path
     *          The path to the svg file
     */
    public SVGLatexConverter(String path) {
        
        // read content of file
        BufferedReader br = null;
        
        try {
            String currentLine;
            StringBuilder sb = new StringBuilder();
            
            br = new BufferedReader(new FileReader(path));
            
            while ((currentLine = br.readLine()) != null) {
                // don't add newlines or tabs to stringbuilder
                sb.append(currentLine
                        .replace(System.getProperty("line.separator"), "")
                        .replace("\t", ""));
            }
            
            svgContent = sb.toString();
        } catch (Exception e) {
            // TODO?
            return;
        } finally {
            try {
                if (br != null) { br.close(); }
            } catch (Exception e) {
                // TODO?
                return;
            }
        }
        
        
        // extract all texttags
        Pattern textTag = Pattern.compile("<text [^>]*><tspan [^>]*>"
                                        + "[^<]*</tspan></text>");
        
        Matcher matcher = textTag.matcher(svgContent);
        while (matcher.find())  {
            // TODO: replace the match with the result of parseLatexText
            System.out.println(parseLatexText(matcher.group()));
        }        
    }
    
    /**
     * Converts a svg texttag like <text ...> foo < /text> into the suitable
     * latexrepresentation.
     * 
     * @param textTag
     *          The textTag with raw latex data.
     * @return
     *          A string with suitable svg tags for displaying latex data
     *          correctly.
     */
    private String parseLatexText(String textTag) {
        // extract latex-text from svg texttags in the form 
        // <text ..><tspan..>LATEX</tspan></text>
        Pattern latexTag = Pattern.compile("<text [^>]*><tspan [^>]*>"
                                         + "([^<]*)</tspan></text>");
        
        Matcher matcher = latexTag.matcher(svgContent);
        if (matcher.find()) {
            String text = matcher.group(1);
            // TODO: parse text into svg tags like the old ISGCI did
        } else {
            // should not happen
            return textTag;
        }
        
        // TODO
        return null;
    }
    
    /**
     * The modes in which the latexstring can be in.
     */
    private enum LatexMode {
        /** x_3 . */
        SUB, 
        /** x^3 .*/
        SUPER, 
        /** co(x) with line over x.*/
        CO;
    }
}
