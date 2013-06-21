/*
 * Creates buttons with text and icons.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */


package teo.isgci.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates buttons with text and icons.
 */
public final class IconButtonFactory {
    /** The relative path to where all icons saved. */
    private static final String ICONPATH = "/icons/";

    /** Filename for AlignCenterIcon. */
    public static final String ALIGN_CENTER_ICON = "AlignCenter.gif";
    
    /** Filename for HelpIcon. */
    public static final String HELP_ICON = "Help.gif";
    
    /** Filename for InformationIcon. */
    public static final String INFORMATION_ICON = "Information.gif";
    
    /** Filename for NewIcon. */
    public static final String NEW_ICON = "New.gif";
    
    /** Filename for OpenIcon. */
    public static final String OPEN_ICON = "Open.gif";
    
    /** Filename for PreferencesIcon. */
    public static final String PREFERENCES_ICON = "Preferences.gif";
    
    /** Filename for PrintIcon. */
    public static final String PRINT_ICON = "Print.gif";
    
    /** Filename for PropertiesIcon. */
    public static final String PROPERTIES_ICON = "Properties.gif";
    
    /** Filename for SaveIcon. */
    public static final String SAVE_ICON = "Save.gif";
    
    /** Filename for SaveAllIcon. */
    public static final String SAVE_ALL_ICON = "SaveAll.gif";
    
    /** Filename for UndoIcon. */
    public static final String UNDO_ICON = "Undo.gif";

    /** Filename for RedoIcon. */
    public static final String REDO_ICON = "Redo.gif";

    /** Filename for ExportIcon. */
    public static final String EXPORT_ICON = "Export.gif";
    
    /** Filename for TipIcon. */
    public static final String TIP_ICON = "Tip.gif";

    /** Filename for RefreshIcon. */
    public static final String REFRESH_ICON = "Refresh.gif";
    
    /** Filename for ResetIcon. */
    public static final String RESET_ICON = "Reset.gif";

    /** Filename for HomeIcon. */
    public static final String HOME_ICON = "Home.gif";
    
    /** Filename for Zoom In Icon. */
    public static final String ZOOM_IN_ICON = "ZoomIn.gif";

    /** Filename for Zoom Out Icon. */
    public static final String ZOOM_OUT_ICON = "ZoomOut.gif";

    /** Filename for Zoom Icon. */
    public static final String ZOOM_ICON = "Zoom.gif";

    /** Filename for Delete Icon. */
    public static final String DELETE_ICON = "Remove.gif";

    /** Filename for Search Icon. */
    public static final String SEARCH_ICON = "Find.gif";

    /** Filename for Add Icon. */
    public static final String ADD_ICON = "Add.gif";
    
    /**
     * Creates a new button with specified icon as image.
     * 
     * @param icon
     *          Relative path to icon.
     * @param description
     *            The description of the ImageIcon and the tooltip text for the
     *            button.
     * @return
     *  A new JButton created from icon and tooltip or a button with 
     *          ? if the path was invalid
     */
    public static JButton createImageButton(String icon, String description) {
        return createImageButton(icon, "", description);
    }
    
    
    /**
     * Returns a new Button with imageicon, or a button with the name 
     * if the path was invalid.
     * 
     * @param icon
     *            The relative path to {@link #ICONPATH} to the icon
     *            
     * @param name
     *            A name that will be displayed next to the icon. Can be empty.
     *            
     * @param description
     *            The description of the ImageIcon and the tooltip text for the
     *            button.
     * 
     * @return A new JButton created from icon, name and tooltip or a button 
               with ? if the path was invalid
     */
    public static JButton createImageButton(String icon, String name,
            String description) {
        String path = ICONPATH + icon;
        JButton button;

        IconButtonFactory factory = new IconButtonFactory();
        
        java.net.URL imgURL = factory.getClass().getResource(path);
        
        if (imgURL != null) {
            
            ImageIcon imgIcon = new ImageIcon(imgURL, description);
            button = new JButton(name, imgIcon);
            
        } else {
            
            System.err.println("Couldn't find file: " + path);
            if (name.isEmpty()) {
                button = new JButton("?");
            } else {
                button = new JButton(name);
            }
        }

        button.setToolTipText(description);
        return button;
    }
    
    /**
     * Internal constructor for getClass().getResource() and to make
     * creating objects outside of this class impossible.
     */
    private IconButtonFactory() { }
}
