/*
 * Reads a svg file and converts all raw latex strings into svg-readable
 * and displayable text.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import teo.isgci.gui.LatexGraphics;
import teo.isgci.gui.SVGGraphics;

/**
 * Reads a svg file and converts all raw latex strings into svg-readable
 * and displayable text.
 * 
 * TODO: static >= object?
 */
public final class SVGLatexConverter {
    
    /** The contents of the svg file. */
    private static String svgContent;
    
    /** Block constructor for utility class. */
    @SuppressWarnings("unused")
    private SVGLatexConverter() { };
    
    /**
     * Converts all latexstring in the svg file on the given path to suitable, 
     * svg-drawable text-graphic.
     * @param path
     *          The path to the svg file
     */
    public static void convertSVGToLatex(String path) {
        
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
            System.err.println("Error opening the file!");
            e.printStackTrace();
            return;
        } finally {
            try {
                if (br != null) { br.close(); }
            } catch (Exception e) {
                System.err.println("Error closing the filestream!");
                e.printStackTrace();
                return;
            }
        }
        
        
        // extract all texttags
        Pattern textTag = Pattern.compile("<text [^>]*><tspan [^>]*>"
                                        + "[^<]*</tspan></text>");
        
        Matcher matcher = textTag.matcher(svgContent);
        while (matcher.find())  {
            String parsedText = parseLatexText(matcher.group());
            svgContent = svgContent.replace(matcher.group(), parsedText);
        }
        
        BufferedWriter bw = null;
        
        try {
            File file = new File(path);

            // file should exist, but still
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(svgContent);

        } catch (Exception e) {
            System.err.println("Error writing to file!");
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) { bw.close(); }
            } catch (Exception e) {
                System.err.println("Error closing writer filestream!");
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * Converts a svg texttag like {@code <text ...> foo < /text>} into the 
     * suitable latexrepresentation.
     * 
     * @param tag
     *          The complete tag with raw latex data and {@code <text> } tag.
     * @return
     *          A string with suitable svg tags for displaying latex data
     *          correctly.
     */
    private static String parseLatexText(String tag) {
        // extract latex-text from svg texttags in the form 
        // <text ..><tspan..>LATEX</tspan></text>
        Pattern latexTag = Pattern.compile("<text [^>]*><tspan [^>]*>"
                                         + "([^<]*)</tspan></text>");
         
        Matcher contentMatcher = latexTag.matcher(tag);
        
        if (contentMatcher.find()) {
            String latexContent = contentMatcher.group(1);

//            // extract the attributes out of the <text> tag
//            Pattern textTag = Pattern.compile("<text ([^>]*)>");
//            Matcher textMatcher = textTag.matcher(tag);
//            
//            if (textMatcher.find()) {
//                // save attributes in hashmap
//                HashMap<String, String> attributes 
//                    = new HashMap<String, String>();
//
//                for (String attribute : textMatcher.group(1).split(" ")) {
//                    String[] content = attribute.split("=");
//
//                    // invalid attribute? should be of form attr="val"
//                    if (content.length != 2) {
//                        System.err.println("Encountered wrong attribute: " 
//                                           + attribute);
//                        continue;
//                    }
//
//                    attributes.put(content[0], content[1]);
//                }
//            }
            
            // extract position from tspan tag
            Pattern xTag = Pattern.compile("<tspan[^x]*x=\"([^\"]+).*");
            Pattern yTag = Pattern.compile("<tspan[^y]*y=\"([^\"]+).*");
            Matcher xMatcher = xTag.matcher(tag);
            Matcher yMatcher = yTag.matcher(tag);
            
            // no position found
            if (!xMatcher.find() || !yMatcher.find()) {
                System.err.println("No valid position found!");
                return tag;
            }
            
            // try to set position
            int positionX, positionY;
            
            try {
                positionX = Integer.parseInt(xMatcher.group(1));
                positionY = Integer.parseInt(yMatcher.group(1));
            } catch (Exception e) {
                System.err.println("Error parsing the svg document!");
                return tag;
            }
            
            
            // build the latex string 
            SVGGraphics graphics = new SVGGraphics();
            
            LatexGraphics lg = LatexGraphics.getInstance();
            lg.drawLatexString(graphics, latexContent, positionX, positionY);
            return graphics.getContent();
        } else {
            // should not happen
            return tag;
        }
    }
    
}

/* EOF */
