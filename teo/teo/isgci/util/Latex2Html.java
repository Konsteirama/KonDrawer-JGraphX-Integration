/*
 * Convert latex to html.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/util/Latex2Html.java,v 2.0 2011/09/25 12:33:43 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.util;

/**
 * Convert latex to html.
 */
public final class Latex2Html extends Latex {

    /**
     * Path to the location of the image files. Should end in /.
     */
    private static final String IMGPATH = "images/";

    /**
     * The current instance of Latex2Html, needed for singleton pattern.
     */
    private static final Latex2Html INSTANCE = new Latex2Html();
    
    /**
     * Indicates whether parser is in "span"/co mode so _6 will not be drawn
     * with a bar over the character.
     */
    private static boolean isInSpan;
    
    /**
     * Create a new latex->html converter. Private because of singleton
     * pattern.
     */
    private Latex2Html() {
        super();
    }

    /**
     * Gets the instance of the Latex2Html converter. Will create a new 
     * instance if there is none.
     * 
     * @return
     *          The current instance of Latex2Html
     */
    public static Latex2Html getInstance() {       
        return INSTANCE;
    }

    /**
     * Return s as an html string.
     */
    public String html(String s) {
        HtmlState state = new HtmlState(s);
        drawLatexPart(state, true);
        return state.target.toString();
    }
    
    /**
     * Ends the span mode; see {@link #isInSpan}.
     * @param s
     *          The state to which the closing tag will be added.
     */
    private void endSpan(State s) {
        if (isInSpan) {
            ((HtmlState) s).target.append("</span>");
            isInSpan = false;
        }
    }

    @Override
    protected State startSuper(State s) {
        endSpan(s);
        ((HtmlState) s).target.append("<sup>");
        return super.startSuper(s);
    }

    @Override
    protected void endSuper(State s) {
        ((HtmlState) s).target.append("</sup>");
        super.endSuper(s);
    }

    @Override
    protected State startSub(State s) {
        endSpan(s);
        ((HtmlState) s).target.append("<sub>");
        return super.startSub(s);
    }

    @Override
    protected void endSub(State s) {
        ((HtmlState) s).target.append("</sub>");
        super.endSub(s);
    }

    @Override
    protected State startCo(State s) {
        ((HtmlState) s).target.append("<span style=\"text-decoration: "
                                      + "overline\">");
        isInSpan = true;
        return super.startCo(s);
    }

    @Override
    protected void endCo(State s) {
        endSpan(s);
        super.endCo(s);
    }

    @Override
    protected void drawPlainString(State state, String str) {
        ((HtmlState) state).target.append(str);
    }

    @Override
    protected void drawGlyph(State state, LatexGlyph g) {
        StringBuffer t = ((HtmlState) state).target;
        if (!g.getHtml().equals("")) {
            t.append(g.getHtml());
        } else {
            t.append(
                "<img src=\"");
            t.append(IMGPATH);
            t.append(g.getImageName());
            t.append("\" alt=\"");
            t.append(g.getName());
            t.append("\"/>");
        }
    }

    protected class HtmlState extends Latex.State {
        protected StringBuffer target;

        public HtmlState(String s) {
            super(s);
            target = new StringBuffer();
        }

        public HtmlState(State parent) {
            super(parent);
            this.target = ((HtmlState) parent).target;
        }

        /**
         * Derive a new state and return it.
         */
        public State deriveStart() {
            return new HtmlState(this);
        }
    }
}


/* EOF */
