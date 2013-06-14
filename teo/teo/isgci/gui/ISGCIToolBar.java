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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import teo.isgci.drawing.DrawingLibraryInterface;
import teo.isgci.drawing.GraphManipulationInterface;

/**
 * ISGCI-specific implementation of the JToolBar that modifies the application
 * and the current DrawingLibraryInterface.
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

    /** Reference to parent-ISGCI Mainframe for opening dialogs etc. */
    private ISGCIMainFrame mainframe;
    
    /** The currently active drawinglibraryinterface. */
    private GraphManipulationInterface<?, ?> graphManipulation;

    /** Undo button, reference here to disable/enable. */
    private JButton undoButton;

    /** Redo button, reference here to disable/enable. */
    private JButton redoButton;

    /**
     * Creates a toolbar with icons that influence both ISGCI and the currently
     * active drawinglibraryinterface (the active tab). The parent needs an
     * initialized tabbedpane!
     * 
     * @param parent
     *            The parent mainframe, to which the toolbar is added.
     */
    public ISGCIToolBar(final ISGCIMainFrame parent) {
        // set basic layout
        setFloatable(false);
        setRollover(true);
        addButtons();

        mainframe = parent;
        
        parent.getTabbedPane().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                DrawingLibraryInterface<?, ?> drawinglib 
                    = parent.getTabbedPane()
                        .getActiveDrawingLibraryInterface();

                // no tab active / startpage
                if (drawinglib == null) {
                    return;
                }
                
                graphManipulation = drawinglib.getGraphManipulationInterface();
                
                // enable/disable undo/redo buttons
                //redoButton.setEnabled(graphManipulation.canRedo());
                //undoButton.setEnabled(graphManipulation.canUndo());
            }
        });
    }

    /**
     * Adds buttons to the toolbar.
     */
    private void addButtons() {
        addGeneralButtons();
        addSeparator();
        
        addGraphControlButtons();
        addSeparator();
        
        addZoomControls();
        addSeparator();
        
        addMiscButtons();
    }

    /**
     * Adds general buttons like "create new drawing" or export to
     * the toolbar.
     */
    private void addGeneralButtons() {
        // NEW DRAWING
        String newDrawingTooltip = "Open a dialog to create a new drawing";
        JButton newdrawingbutton = createImageButton(NEW_DRAWING_ICON,
                newDrawingTooltip);
        add(newdrawingbutton);

        newdrawingbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.openSelectGraphClassesDialog();
            }
        });
        
        // EXPORT
        String exportTooltip = "Open the export dialog";
        JButton exportbutton = createImageButton(EXPORT_ICON, exportTooltip);
        add(exportbutton);

        exportbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.openExportDialog();
            }
        });
    }
    
    /**
     * Adds controls like undo and redo to the toolbar.
     */
    private void addGraphControlButtons() {
        // UNDO
        String undoTooltip = "Undo the last action";
        undoButton = createImageButton(UNDO_ICON, undoTooltip);
        // undoButton.setEnabled(false);
        add(undoButton);

        undoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    // undo, if possible
                    if (graphManipulation.canUndo()) {
                        graphManipulation.undo();
                    }

                    // disable button if no more undos possible
                    // undoButton.setEnabled(graphManipulation.canUndo());
                }
            }
        });

        // REDO
        String redoTooltip = "Redo the last undo action";
        redoButton = createImageButton(REDO_ICON, redoTooltip);
        // redoButton.setEnabled(false);
        add(redoButton);

        redoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    // undo, if possible
                    if (graphManipulation.canRedo()) {
                        graphManipulation.redo();
                    }

                    // disable button if no more undos possible
                    // redoButton.setEnabled(graphManipulation.canRedo());
                }
            }
        });
        
        // REAPPLYLAYOUT
        String resetTooltip = "Reset the graph's layout";
        JButton resetbutton = createImageButton(RESET_ICON, resetTooltip);
        add(resetbutton);

        resetbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    graphManipulation.reapplyHierarchicalLayout();
                    
                }
            }
        });

        // RESTORELAYOUT
        String restoreTooltip = "Restore the graph to its original state";
        JButton restorebutton 
            = createImageButton(RESTORE_ICON, restoreTooltip);
        add(restorebutton);

        restorebutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    graphManipulation.resetLayout();
                }
            }
        });
    }
    
    /**
     * Adds zooming-related controls to the toolbar.
     */
    private void addZoomControls() {
        // ZOOM IN
        String zoomInTooltip = "Zoom In";
        JButton zoominbutton = createImageButton(ZOOM_IN_ICON, zoomInTooltip);
        add(zoominbutton);

        zoominbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    graphManipulation.zoom(true);
                }
            }
        });

        // ZOOM OUT
        String zoomOutTooltip = "Zoom Out";
        JButton zoomoutbutton = createImageButton(ZOOM_OUT_ICON,
                zoomOutTooltip);
        add(zoomoutbutton);

        zoomoutbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (graphManipulation != null) {
                    graphManipulation.zoom(false);
                }
            }
        });

        // ZOOM
        String zoomTooltip = "?";
        JButton zoombutton = createImageButton(ZOOM_ICON, zoomTooltip);
        add(zoombutton);

        zoombutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });

        // ZOOM TO FIT
        String zoomToFitTooltip = "Zoom to fit everything into the canvas";
        JButton zoomtofitbutton = createImageButton(ZOOM_TO_FIT_ICON, 
                "Zoom to fit", zoomToFitTooltip);
        add(zoomtofitbutton);

        zoomtofitbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }
    
    /**
     * Add controls that have no specific group.
     */
    private void addMiscButtons() {
        // DELETE
        String deleteTooltip = "?";
        JButton deletebutton 
            = createImageButton(DELETE_ICON, deleteTooltip);
        add(deletebutton);

        deletebutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });

        // SEARCH
        String searchTooltip = "Searches for the specific node in the canvas.";
        JButton searchbutton 
            = createImageButton(SEARCH_ICON, searchTooltip);
        add(searchbutton);

        searchbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }
    
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
    private JButton createImageButton(String icon, String description) {
        return createImageButton(icon, "", description);
    }
    
    /**
     * Returns a new Button with imageicon, or a button with ? if the path
     * was invalid.
     * 
     * @param icon
     *            The relative path to {@link #ICONPATH} to the icon
     *            
     * @param name
     *            A name that will be displayed next to the icon. Can be null.
     *            
     * @param description
     *            The description of the ImageIcon and the tooltip text for the
     *            button.
     * 
     * @return A new JButton created from icon, name and tooltip or a button 
               with ? if the path was invalid
     */
    private JButton createImageButton(String icon,
                                      String name,
                                      String description) {
        String path = ICONPATH + icon;
        JButton button;

        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon imgIcon = new ImageIcon(imgURL, description);

            button = new JButton(imgIcon);
            // TODO
            //button.add(new JLabel(name));
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
}

/* EOF */
