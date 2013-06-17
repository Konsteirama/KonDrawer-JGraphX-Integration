/*
 * A complexity class.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/problem/Complexity.java,v 2.3 2012/10/28 16:00:57 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.problem;

import java.awt.Color;


/**
 * Represents complexity classes like Linear, P, NPC, coNPC and Unknown.
 * Use only the defined comparison methods, the enum compareTo does not give a
 * complexity comparison.
 * The betterThan and betterOrEqual compare as LIN < P < GIC < NPC,NPH,CONPC.
 * betterThan with other complexities is undefined.
 */
public enum Complexity {
    /** Higher complexity, higher number */
    /** Linear. */
    LINEAR ("Linear",        "Lin",   "Bounded", Color.green),
    
    /** Polynomial. */
    P      ("Polynomial",    "P",     "Bounded", Color.green.darker()),
    
    /** GI-complete. */
    GIC    ("GI-complete",   "GIC",   "Unbounded", Color.red.brighter()),
    
    /** NP-complete. */
    NPC    ("NP-complete",   "NPC",   "Unbounded", Color.red),
    
    /** NP-hard. */
    NPH    ("NP-hard",       "NPh",   "Unbounded", Color.red),
    
    /** coNP-complete. */
    CONPC  ("coNP-complete", "coNPC", "Unbounded", Color.red),
    
    /** Open. */
    OPEN   ("Open",          "Open",  "Open", Color.white),
    
    /** Unknown. */
    UNKNOWN("Unknown",       "?",     "Unknown", Color.white);


    /** Complexity class */
    /** Full name. */
    private String name;
    /** Abbreviation. */
    private String abbrev;
    /** Widthname like bounded or unbounded. */
    private String widthName;
    /** Default color for visualization. */
    private Color defaultColor;


    /**
     * Creates a new complexity with the given value and names and default
     * color.
     * @param complexityName
     *          Full name of the complexity.
     * @param abbreviation
     *          Abbreviation of the complexity.
     * @param width
     *          Widthname of the complexity.
     */
    private Complexity(String complexityName,
            String abbreviation, String width) {
        this(complexityName, abbreviation, width, Color.white);
    }

    /**
     * Creates a new complexity with the given value and names and default
     * color.
     * @param complexityName
     *          Full name of the complexity.
     * @param abbreviation
     *          Abbreviation of the complexity.
     * @param width
     *          Widthname of the complexity.
     * @param color
     *          The color of the complexity.
     */
    private Complexity(String complexityName,
            String abbreviation, String width, Color color) {
        name = complexityName;
        abbrev = abbreviation;
        widthName = width;
        defaultColor = color;
    }

    /**
     * Gets the short name of the complexity.
     * @return
     *          The abbreviaton of the complexity
     */
    public String getShortString() {
        return abbrev;
    }

    /**
     * Gets the full name of the complexity.
     * @return
     *          The name of the complexity
     */
    public String getComplexityString() {
        return name;
    }

    /**
     * Gets the width of the complexity.
     * @return
     *          Bounded, unbounded, open or unknown
     */
    public String getWidthString() {
        return widthName;
    }

    /**
     * Gets the default color of the complexity.
     * @return
     *          The default color.
     */
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    /**
     * Calculates whether this complexity is better than c.
     * 
     * @param c
     *          The complexity to be compared with
     * 
     * @return
     *          True, if this complexity is greater than c, else false.
     */
    public boolean betterThan(Complexity c) {
        return compareTo(c) < 0;
    }

    /**
     * Calculates whether this complexity is better or equal than c.
     * 
     * @param c
     *          The complexity to be compared with
     * 
     * @return
     *          True, if this complexity is greater or equal to c, 
     *          else false.
     */
    public boolean betterOrEqual(Complexity c) {
        return compareTo(c) <= 0;
    }

    /**
     * Checks if the complexity is unknown.
     * @return
     *          True if it's unknown or open, else false.
     */
    public boolean isUnknown() {
        return this == UNKNOWN  ||  this == OPEN;
    }

    /**
     * Checks if the complexity is open.
     * @return
     *          True if it's open, else false.
     */
    public boolean isOpen() {
        return this == OPEN;
    }

    /**
     * Checks if the complexity is NPC.
     * @return
     *          True if it's NPC, else false.
     */
    public boolean isNPC() {
        return this == NPC;
    }

    /**
     * Checks if the complexity is CONPC.
     * @return
     *          True if it's CONPC, else false.
     */
    public boolean isCONPC() {
        return this == CONPC;
    }

    /**
     * Checks if the complexity is could not be in P.
     * @return
     *          True if it's probably not in P, else false.
     */
    public boolean likelyNotP() {
        return this == CONPC  || this == NPC  ||  this == NPH  ||  this == GIC;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Can a problem at the same time have this complexity and c's?
     * 
     * @param c
     *          The complexity to be compared with
     * 
     * @return
     *          True if it's compatible, else false.
     */
    public boolean isCompatible(Complexity c) {
        return this == UNKNOWN  ||  c == UNKNOWN  
                || (betterOrEqual(P)  &&  c.betterOrEqual(P)) 
                || equals(c);
    }

    /**
     * Assuming this complexity is assigned to some graphclasses, return true
     * iff this complexity also holds for subclasses.
     * 
     * @return
     *          True if it's better or equal to P, else false.
     */
    boolean distributesDown() {
        return betterOrEqual(P);
    }

    /**
     * Assuming this complexity is assigned to some graphclasses, return true
     * iff this complexity also holds for superclasses.
     * 
     * @return 
     *          True if it's likely not in p, else false.
     */
    boolean distributesUp() {
        return likelyNotP();
    }

    /**
     * Assuming this complexity is assigned to some graphclasses, return true
     * iff this complexity also holds for equivalent classes, but not
     * necessarily for super/sub classes.
     * 
     * @return
     *          True if it's open, else false.
     */
    boolean distributesEqual() {
        return isOpen();
    }


    /**
     * If a problem has both complexity this and c, return the resulting
     * complexity.
     * 
     * @param c
     *          The complexity to be compared with.
     * 
     * @return
     *          The resulting complexity.
     */
    public Complexity distil(Complexity c) {
        if (!isCompatible(c)) {
            throw new ComplexityClashException(this, c);
        }
        if (c.isUnknown()) {
            return this;
        }
        if (c.betterThan(this)  ||  this.isUnknown()) {
            return c;
        }
        return this;
    }


    /**
     * Return the complexity class represented by s.
     * 
     * @param s
     *          The name of the complexity.
     * 
     * @return
     *          The matching complexity or null if none is found.
     */
    public static Complexity getComplexity(String s) {
        for (Complexity c : Complexity.values()) {
            if (c.name.equals(s)  ||  c.abbrev.equals(s)) {
                return c;
            }
        }
        if (LINEAR.widthName.equals(s)) {
            return LINEAR;
        }
        if (NPC.widthName.equals(s)) {
            return NPC;
        }
        
        return null;
    }
}

/* EOF */
