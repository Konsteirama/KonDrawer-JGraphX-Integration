/*
 * Manages all UserSettings with public getter and setter.  If a setting 
 * changes, it notifies all subscribed objects via the Updatable interface
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import teo.isgci.db.Algo;
import teo.isgci.db.Algo.NamePref;
import teo.isgci.problem.Complexity;
import teo.isgci.problem.Problem;

/**
 * Manages all UserSettings with public getter and setter. If a setting
 * changes, it notifies all subscribed objects via the {@link #Updatable}
 */
public abstract class UserSettings {

    // Settings
    // ------------------------------------------------

    /**
     * The colorblind color for highlighting.
     */
    public static final Color BLINDHIGHTLIGHTCOLOR = new Color(240, 39, 100);
    /**
     * The currently active tab.
     */
    private static JComponent activeTab;
    /**
     * The color of the font of all drawn vertices.
     */
    private static Color backgroundColor = getDefaultBackgroundColor();
    /**
     * Maps a complexity to the corresponding color.
     */
    private static HashMap<Complexity, Color> complexityToColor
            = getDefaultColorScheme();
    /**
     * The default naming preference which is used for new tabs.
     */
    private static Algo.NamePref defaultNamePref = Algo.NamePref.BASIC;
    /**
     * The color of the font of all drawn vertices.
     */
    private static Color fontColor = getDefaultFontColor();
    /**
     * The standard color for highlighting neighbour and edges.
     */
    private static Color highlightColor = getDefaultHighlightColor();
    /**
     * Maps the content of the tabs to their corresponding
     * naming preference.
     */
    private static HashMap<JComponent, Algo.NamePref> panelToNamingPref
            = new HashMap<JComponent, Algo.NamePref>();
    /**
     * Maps the tab to their corresponding Problem.
     */
    private static HashMap<JComponent, Problem> panelToProblem
            = new HashMap<JComponent, Problem>();
    /**
     * The standard color for marking the selected nodes.
     */
    private static Color selectionColor = getDefaultSelectionColor();

    // Getter / Setter
    // ------------------------------------------------
    /**
     * The objects that have to be notified when a setting changes.
     */
    private static List<Updatable> updatables = new LinkedList<Updatable>();

    /**
     * Returns the color of the given complexity.
     *
     * @param complexity the complexity for which the color is wanted
     * @return the color which is mapped to the given complexity
     */
    public static Color getColor(Complexity complexity) {
        return complexityToColor.get(complexity);
    }

    /**
     * Returns the current mapping from complexities to their colors.
     *
     * @return the current color scheme
     */
    public static HashMap<Complexity, Color> getColorScheme() {
        return complexityToColor;
    }

    /**
     * Sets the color scheme to the given mapping.
     *
     * @param colorScheme the new color scheme
     */
    public static void setColorScheme(HashMap<Complexity, Color> colorScheme) {
        // DO NOT USE colorScheme.keySet() - it's null with enums!
        for (Complexity complexity : Complexity.values()) {
            complexityToColor.put(complexity, colorScheme.get(complexity));
        }

        updateSettings();
    }

    /**
     * Getter for {@link #backgroundColor}.
     *
     * @return The current font color.
     */
    public static Color getCurrentBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Setter for {@link #backgroundColor}.
     *
     * @param color The background color.
     */
    public static void setCurrentBackgroundColor(Color color) {
        backgroundColor = color;
    }

    /**
     * Getter for {@link #fontColor}.
     *
     * @return The current font color.
     */
    public static Color getCurrentFontColor() {
        return fontColor;
    }

    /**
     * Setter for {@link #fontColor}.
     *
     * @param color The font color.
     */
    public static void setCurrentFontColor(Color color) {
        fontColor = color;
    }

    /**
     * Getter for {@link #highlightColor}.
     *
     * @return The current highlight color.
     */
    public static Color getCurrentHighlightColor() {
        return highlightColor;
    }

    /**
     * Setter for {@link #highlightColor}.
     *
     * @param color The highlight color.
     */
    public static void setCurrentHighlightColor(Color color) {
        highlightColor = color;
    }

    /**
     * Getter for {@link #selectionColor}.
     *
     * @return The current selection color.
     */
    public static Color getCurrentSelectionColor() {
        return selectionColor;
    }

    /**
     * Setter for {@link #selectionColor}.
     *
     * @param color The selection color.
     */
    public static void setCurrentSelectionColor(Color color) {
        selectionColor = color;
    }

    /**
     * Getter for default background color.
     *
     * @return The default background color.
     */
    public static Color getDefaultBackgroundColor() {
        return Color.white;
    }

    /**
     * Returns the default coloring scheme for the colorblind mode.
     *
     * @return a hashmap in which each complexity is mapped to a color.
     */
    public static HashMap<Complexity, Color>
    getDefaultColorBlindColorScheme() {
        HashMap<Complexity, Color> defaultComplexityToColor
                = new HashMap<Complexity, Color>();

        final Color linearcolor = new Color(253, 184, 99);
        final Color pcolor = new Color(230, 97, 1);
        final Color conpccolor = new Color(94, 60, 153);
        final Color npccolor = new Color(94, 60, 153);
        final Color nphcolor = new Color(94, 60, 153);
        final Color giccolor = new Color(178, 171, 210);

        defaultComplexityToColor.put(Complexity.LINEAR, linearcolor);

        defaultComplexityToColor.put(Complexity.UNKNOWN, Color.white);
        defaultComplexityToColor.put(Complexity.OPEN, Color.white);

        defaultComplexityToColor.put(null, Color.white);

        defaultComplexityToColor.put(Complexity.P, pcolor);

        defaultComplexityToColor.put(Complexity.CONPC, conpccolor);
        defaultComplexityToColor.put(Complexity.NPC, npccolor);
        defaultComplexityToColor.put(Complexity.NPH, nphcolor);

        defaultComplexityToColor.put(Complexity.GIC, giccolor);

        return defaultComplexityToColor;
    }

    /**
     * Returns the default coloring scheme.
     *
     * @return a hashmap in which each complexity is mapped to a color.
     */
    public static HashMap<Complexity, Color> getDefaultColorScheme() {
        HashMap<Complexity, Color> defaultComplexityToColor
                = new HashMap<Complexity, Color>();

        for (Complexity c : Complexity.values()) {
            defaultComplexityToColor.put(c, c.getDefaultColor());
        }

        return defaultComplexityToColor;
    }

    /**
     * Getter for default font color.
     *
     * @return The default font color.
     */
    public static Color getDefaultFontColor() {
        return Color.black;
    }

    /**
     * Getter for default highlight color.
     *
     * @return The default highlight color.
     */
    public static Color getDefaultHighlightColor() {
        return Color.orange;
    }

    /**
     * Returns the default naming preference which should be used for new tabs.
     *
     * @return the default naming preference
     */
    public static Algo.NamePref getDefaultNamingPref() {
        return defaultNamePref;
    }

    /**
     * Getter for default selection color.
     *
     * @return The default selection color.
     */
    public static Color getDefaultSelectionColor() {
        return Color.black;
    }

    /**
     * Gets the naming preference for the given tab.
     *
     * @param tab the currently selected tab
     * @return the naming preference for activeTab
     */
    public static NamePref getNamingPref(JComponent tab) {
        if (panelToNamingPref.get(activeTab) != null) {
            return panelToNamingPref.get(activeTab);
        } else {
            return getDefaultNamingPref();
        }
    }

    /**
     * Gets the problem for the currently active tab.
     *
     * @return the problem for the tab, or null if no problem is assigned.
     */
    public static Problem getProblem() {
        return panelToProblem.get(activeTab);
    }

    /**
     * Sets the problem for the currently active tab.
     *
     * @param problem the problem for the active tab
     */
    public static void setProblem(Problem problem) {
        panelToProblem.put(activeTab, problem);
        updateSettings();
    }

    /**
     * Sets the currently active tab.
     *
     * @param tab the active tab.
     */
    public static void setActiveTab(JComponent tab) {
        activeTab = tab;
    }

    /**
     * Sets a new color for the given complexity.
     *
     * @param complexity the complexity for which the color is changed.
     * @param color      the new color for the given complexity
     */
    public static void setColor(Complexity complexity, Color color) {
        if (!color.equals(complexityToColor.get(complexity))) {
            complexityToColor.put(complexity, color);
            updateSettings();
        }
    }

    // Updatable
    // ------------------------------------------------

    /**
     * Sets the default naming preference.
     *
     * @param tab      the currently selected tab
     * @param namepref the new default naming preference
     */
    public static void setNamingPref(JComponent tab,
                                     Algo.NamePref namepref) {
        if (namepref != null) {
            panelToNamingPref.put(activeTab, namepref);
            updateSettings();
        }
    }

    /**
     * Adds an object to the update list.
     *
     * @param instance object to be added to the list
     */
    public static void subscribeToOptionChanges(Updatable instance) {
        updatables.add(instance);
    }

    /**
     * Removes an object from the update list.
     *
     * @param instance object to be removed from the list
     */
    public static void unsubscribeFromOptionChanges(Updatable instance) {
        updatables.remove(instance);
    }

    /**
     * Notifies all subscribed objects of a change in the UserSettings.
     */
    private static void updateSettings() {
        for (Updatable instance : updatables) {
            instance.updateOptions();
        }
    }
}

/* EOF */
