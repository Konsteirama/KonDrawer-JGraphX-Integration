/*
 * ISGCI-specific implementation of the JToolBar that modifies the 
 * application and the current DrawingLibraryInterface.
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
import javax.swing.JToolBar;

/**
 * ISGCI-specific implementation of the JToolBar that modifies the 
 * application and the current DrawingLibraryInterface.
 */
public class ISGCIToolBar extends JToolBar {
    /**
     * Change this every time this class is changed.
     */
    private static final long serialVersionUID = 1L;

    /** The relative path to where all icons saved. */
    private static final String ICONPATH = "/icons/"; 
    
    /** Filename for UndoIcon. */
    private static final String UNDO_ICON = "Undo.gif";
    
    /** Filename for RedoIcon. */
    private static final String REDO_ICON = "Redo.gif";
    
    /** Filename for ExportIcon. */
    private static final String EXPORT_ICON = "Export.gif";
    
    /** Filename for ResetIcon. */
    private static final String RESET_ICON = "Help.gif";
    
    /** Filename for RestoreIcon. */
    private static final String RESTORE_ICON = "Help.gif";
    
    /** Filename for Zoom In Icon. */
    private static final String ZOOM_IN_ICON = "ZoomIn.gif";
    
    /** Filename for Zoom Out Icon. */
    private static final String ZOOM_OUT_ICON = "ZoomOut.gif";
    
    /** Filename for Zoom Icon. */
    private static final String ZOOM_ICON = "Zoom.gif";
    
    /** Filename for Zoom to Fit Icon. */
    private static final String ZOOM_TO_FIT_ICON = "Help.gif";
    
    /** Filename for Delete Icon. */
    private static final String DELETE_ICON = "Remove.gif";
    
    /** Filename for Search Icon. */
    private static final String SEARCH_ICON = "Find.gif";
    
    /** Filename for New Drawing Icon. */
    private static final String NEW_DRAWING_ICON = "Add.gif";
    
    /**
     * Creates a toolbar with icons that influence both ISGCI and the 
     * currently active drawinglibraryinterface (the active tab).
     */
    public ISGCIToolBar() {
        // set basic layout
        setFloatable(false);
        setRollover(true);

        // UNDO
        String undoTooltip = "Undo the last action";
        JButton undobutton = createImageButton(UNDO_ICON, undoTooltip);
        add(undobutton);
        
        // REDO
        String redoTooltip = "Redo the last undo action";
        JButton redobutton = createImageButton(REDO_ICON, redoTooltip);
        add(redobutton);
        
        // ----
        addSeparator();
        
        // EXPORT
        String exportTooltip = "Open the export dialog";
        JButton exportbutton = createImageButton(EXPORT_ICON, exportTooltip);
        add(exportbutton);
        
        // RESET
        String resetTooltip = "Reset the graph's layout";
        JButton resetbutton = createImageButton(RESET_ICON, resetTooltip);
        add(resetbutton);
        
        // RESTORE
        String restoreTooltip = "Restore the graph to its original state";
        JButton restorebutton 
            = createImageButton(RESTORE_ICON, restoreTooltip);
        add(restorebutton);
        
        // ----
        addSeparator();
        
        // ZOOM IN
        String zoomInTooltip = "Zoom In";
        JButton zoominbutton = createImageButton(ZOOM_IN_ICON, zoomInTooltip);
        add(zoominbutton);
        
        // ZOOM OUT
        String zoomOutTooltip = "Zoom Out";
        JButton zoomoutbutton 
            = createImageButton(ZOOM_OUT_ICON, zoomOutTooltip);
        add(zoomoutbutton);
        
        // ZOOM
        String zoomTooltip = "?";
        JButton zoombutton = createImageButton(ZOOM_ICON, zoomTooltip);
        add(zoombutton);
        
        // ZOOM TO FIT
        String zoomToFitTooltip = "Zoom to fit everything into the canvas";
        JButton zoomtofitbutton 
            = createImageButton(ZOOM_TO_FIT_ICON, zoomToFitTooltip);
        add(zoomtofitbutton);
        
        // ----
        addSeparator();
        
        // DELETE
        String deleteTooltip = "?";
        JButton deletebutton = createImageButton(DELETE_ICON, deleteTooltip);
        add(deletebutton);
        
        // SEARCH
        String searchTooltip = "Searches for the specific node in the canvas.";
        JButton searchbutton = createImageButton(SEARCH_ICON, searchTooltip);
        add(searchbutton);
        
        // NEW DRAWING
        String newDrawingTooltip = "Open a dialog to create a new drawing";
        JButton newdrawingbutton 
            = createImageButton(NEW_DRAWING_ICON, newDrawingTooltip);
        add(newdrawingbutton);
    }
    
    /** 
     * Returns an ImageIcon, or null if the path was invalid.
     * 
     * @param icon
     *          The relative path to {@link #ICONPATH} to the icon
     * @param description
     *          The description of the ImageIcon and the tooltip text for
     *          the button.
     * 
     *  @return 
     *  A new JButton created from icon and tooltip or a button with ? if the
     *  path was invalid
     */
    protected JButton createImageButton(String icon, String description) {
        String path = ICONPATH + icon;
        JButton button;
        
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon imgIcon = new ImageIcon(imgURL, description);
            button = new JButton(imgIcon);
        } else {
            System.err.println("Couldn't find file: " + path);
            button = new JButton("?");
        }
        
        button.setToolTipText(description);
        return button;
    }
}

/* EOF */
