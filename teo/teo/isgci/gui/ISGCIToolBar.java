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
        ImageIcon undoicon = createImageIcon(UNDO_ICON, undoTooltip);
        JButton undobutton = new JButton(undoicon);
        undobutton.setToolTipText(undoTooltip);
        add(undobutton);
        
        // REDO
        String redoTooltip = "Redo the last undo action";
        ImageIcon redoicon = createImageIcon(REDO_ICON, "desc");
        JButton redobutton = new JButton(redoicon);
        redobutton.setToolTipText(redoTooltip);
        add(redobutton);
        
        // ----
        addSeparator();
        
        // EXPORT
        String exportTooltip = "Open the export dialog";
        ImageIcon exporticon = createImageIcon(EXPORT_ICON, "desc");
        JButton exportbutton = new JButton(exporticon);
        exportbutton.setToolTipText(exportTooltip);
        add(exportbutton);
        
        // RESET
        String resetTooltip = "Reset the graph's layout";
        ImageIcon reseticon = createImageIcon(RESET_ICON, "desc");
        JButton resetbutton = new JButton(reseticon);
        resetbutton.setToolTipText(resetTooltip);
        add(resetbutton);
        
        // RESTORE
        String restoreTooltip = "Restore the graph to its original state";
        ImageIcon restoreicon = createImageIcon(RESTORE_ICON, "desc");
        JButton restorebutton = new JButton(restoreicon);
        restorebutton.setToolTipText(restoreTooltip);
        add(restorebutton);
        
        // ----
        addSeparator();
        
        // ZOOM IN
        String zoomInTooltip = "Zoom In";
        ImageIcon zoominicon = createImageIcon(ZOOM_IN_ICON, "desc");
        JButton zoominbutton = new JButton(zoominicon);
        zoominbutton.setToolTipText(zoomInTooltip);
        add(zoominbutton);
        
        // ZOOM OUT
        String zoomOutTooltip = "Zoom Out";
        ImageIcon zoomouticon = createImageIcon(ZOOM_OUT_ICON, "desc");
        JButton zoomoutbutton = new JButton(zoomouticon);
        zoomoutbutton.setToolTipText(zoomOutTooltip);
        add(zoomoutbutton);
        
        // ZOOM
        String zoomTooltip = "?";
        ImageIcon zoomicon = createImageIcon(ZOOM_ICON, "desc");
        JButton zoombutton = new JButton(zoomicon);
        zoombutton.setToolTipText(zoomTooltip);
        add(zoombutton);
        
        // ZOOM TO FIT
        String zoomToFitTooltip = "Zoom to fit everything into the canvas";
        ImageIcon zoomtofiticon = createImageIcon(ZOOM_TO_FIT_ICON, "desc");
        JButton zoomtofitbutton = new JButton(zoomtofiticon);
        zoomtofitbutton.setToolTipText(zoomToFitTooltip);
        add(zoomtofitbutton);
        
        // ----
        addSeparator();
        
        // DELETE
        String deleteTooltip = "?";
        ImageIcon deleteicon = createImageIcon(DELETE_ICON, "desc");
        JButton deletebutton = new JButton(deleteicon);
        deletebutton.setToolTipText(deleteTooltip);
        add(deletebutton);
        
        // SEARCH
        String searchTooltip = "Searches for the specific node in the canvas.";
        ImageIcon searchicon = createImageIcon(SEARCH_ICON, "desc");
        JButton searchbutton = new JButton(searchicon);
        searchbutton.setToolTipText(searchTooltip);
        add(searchbutton);
        
        // NEW DRAWING
        String newDrawingTooltip = "Open a dialog to create a new drawing";
        ImageIcon newdrawingicon = createImageIcon(NEW_DRAWING_ICON, "desc");
        JButton newdrawingbutton = new JButton(newdrawingicon);
        newdrawingbutton.setToolTipText(newDrawingTooltip);
        add(newdrawingbutton);
    }
    
    /** 
     * Returns an ImageIcon, or null if the path was invalid.
     * 
     * @param icon
     *          The relative path to {@link #ICONPATH} to the icon
     * @param description
     *          The description of the ImageIcon
     * 
     *  @return 
     *  A new ImageIcon created from icon and tooltip or null, if the path
     *  was invalid.
     */
    protected ImageIcon createImageIcon(String icon, String description) {
        String path = ICONPATH + icon;
        
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {          
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

/* EOF */