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
import java.util.LinkedList;
import java.util.List;



/**
 * Manages all UserSettings with public getter and setter. 
 * If a setting changes, it notifies all subscribed objects via the
 * {@link #Updatable} 
 */
public abstract class UserSettings {
    
    //                  Settings
    // ------------------------------------------------
    
    /**
     * A sample color with sample javadoc.
     * TODO marc delete this property.
     */
    private Color nodeColor = Color.green;
    
    
    //               Getter / Setter
    // ------------------------------------------------
    
    
    /**
     * Sample getter and javadoc.
     * TODO marc delete this getter
     * @return
     *          returns {@link #nodeColor}
     */         
    public Color getNodeColor() {
        return nodeColor;
    }
    
    /**
     * Sample setter and javadoc.
     * TODO marc delete this setter
     * @param value
     *          sets {@link #nodeColor}
     */
    public void setNodeColor(Color value) {
        nodeColor = value;
        updateSettings();
    }
    
    //            Subscribe / Unsubscribe
    // ------------------------------------------------
    
    /**
     * TODO marc.
     * @param instance
     *          TODO marc
     */
    public static void subscribeToOptionChanges(Updatable instance) {
        updatables.add(instance);
    }
    
    /**
     * TODO marc.
     * @param instance
     *          TODO marc
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
