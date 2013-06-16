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

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import teo.isgci.db.Algo;
import teo.isgci.problem.Complexity;

/**
 * Manages all UserSettings with public getter and setter. If a setting 
 * changes, it notifies all subscribed objects via the {@link #Updatable}
 */
public abstract class UserSettings {

    // Settings
    // ------------------------------------------------

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
     * The placement of the tabline.
     */
    private static int tabOrientation = getDefaultTabPlacement();

    /**
     * Indicates, if the toolbar is visible or not.
     */
    private static boolean toolbarShowHide = getDefaultToolbarSetting();

    /**
     * The java theme, that is currently set.
     */
    private static String theme = getDefaultTheme();

    /**
     * The zoomlevel for the graphs, that are drawn in new tabs.
     */
    private static int zoomLevel = getDefaultZoomLevel();

    // Getter / Setter
    // ------------------------------------------------

    /**
     * Getter for default tabline placement.
     * 
     * @return
     *          Returns the default tabline placement.
     */
    public static int getDefaultTabPlacement() {
        return JTabbedPane.TOP;
    }

    /**
     * Returns the current tabline placement.
     * 
     * @return
     */
    public static int getCurrentTabPlacement() {
        return tabOrientation;
    }

    /**
     * 
     * @param tabPlacement
     */
    public static void setTabPlacement(int tabPlacement) {
        tabOrientation = tabPlacement;
        updateSettings();
    }

    /**
     * Returns the default visibility setting.
     * 
     * @return
     */
    public static boolean getDefaultToolbarSetting() {
        return true;
    }

    /**
     * Returns the current state of the visibility of the toolbar.
     * 
     * @return
     */
    public static boolean getCurrentToolbarSetting() {
        return toolbarShowHide;
    }

    /**
     * 
     * @param toolbarSetting
     */
    public static void setToolbarSetting(boolean toolbarSetting) {
        toolbarShowHide = toolbarSetting;
        updateSettings();
    }

    /**
     * Returns the default java crossplatform theme.
     * 
     * @return
     */
    public static String getDefaultTheme() {
        return UIManager.getCrossPlatformLookAndFeelClassName();
    }

    /**
     * Returns the currenly set theme.
     * 
     * @return
     */
    public static String getCurrentTheme() {
        return theme;
    }

    /**
     * 
     * @param newTheme
     */
    public static void setTheme(String newTheme) {
        theme = newTheme;
        updateSettings();
    }

    /**
     * Returns default zoomlevel.
     * 
     * @return
     */
    public static int getDefaultZoomLevel() {
        return 0;
    }

    /**
     * Returns the zoomlevel for new drawn graphs.
     * 
     * @return
     */
    public static int getCurrentZoomLevel() {
        return zoomLevel;
    }

    /**
     * Sets a new zoomlevel for the graphs, that are drawn in the future.
     * 
     * @param newZoomLevel
     */
    public static void setZoomLevel(int newZoomLevel) {
        zoomLevel = newZoomLevel;
        updateSettings();
    }

    /**
     * Sets a new color for the given complexity.
     * 
     * @param complexity
     *            the complexity for which the color is changed.
     * 
     * @param color
     *            the new color for the given complexity
     */
    public static void setColor(Complexity complexity, Color color) {
        complexityToColor.put(complexity, color);
        updateSettings();
    }

    /**
     * Returns the color of the given complexity.
     * 
     * @param complexity
     *            the complexity for which the color is wanted
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
     * @param colorScheme
     *            the new color scheme
     */
    public static void setColorScheme(HashMap<Complexity, Color> colorScheme) {
        for (Complexity complexity : colorScheme.keySet()) {
            complexityToColor.put(complexity, colorScheme.get(complexity));
        }
        updateSettings();
    }

    /**
     * Returns the default coloring scheme.
     * 
     * @return a hashmap in which each complexity is mapped to a color.
     */
    public static HashMap<Complexity, Color> getDefaultColorScheme() {
        HashMap<Complexity, Color> defaultComplexityToColor 
            = new HashMap<Complexity, Color>();

        defaultComplexityToColor.put(Complexity.LINEAR, Color.green);

        defaultComplexityToColor.put(Complexity.UNKNOWN, Color.white);
        defaultComplexityToColor.put(Complexity.OPEN, Color.white);

        defaultComplexityToColor.put(null, Color.white);

        defaultComplexityToColor.put(Complexity.P, Color.green.darker());

        defaultComplexityToColor.put(Complexity.CONPC, Color.red);
        defaultComplexityToColor.put(Complexity.NPC, Color.red);
        defaultComplexityToColor.put(Complexity.NPH, Color.red);

        defaultComplexityToColor.put(Complexity.GIC, Color.red.brighter());

        return defaultComplexityToColor;
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
     * Sets the default naming preference.
     * 
     * @param namepref
     *            the new default naming preference
     */
    public static void setNamingPref(Algo.NamePref namepref) {
        if (namepref != null) {
            defaultNamePref = namepref;
            updateSettings();
        }
    }

    /**
     * Returns the default naming preference which should be used for new tabs.
     * 
     * @return the default naming preference
     */
    public static Algo.NamePref getNamingPref() {
        return defaultNamePref;
    }

    // Subscribe / Unsubscribe
    // ------------------------------------------------

    /**
     * TODO marc.
     * 
     * @param instance
     *            TODO marc
     */
    public static void subscribeToOptionChanges(Updatable instance) {
        System.out.println("bla");
        updatables.add(instance);
    }

    /**
     * TODO marc.
     * 
     * @param instance
     *            TODO marc
     */
    public static void unsubscribeFromOptionChanges(Updatable instance) {
        updatables.remove(instance);
    }

    /**
     * The objects that have to be notified when a setting changes.
     */
    private static List<Updatable> updatables = new LinkedList<Updatable>();

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
