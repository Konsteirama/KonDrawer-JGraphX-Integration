/*
 * Replace this line with a (multi-line) description of this file...
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */


package teo.isgci.drawing;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.JComponent;

/**
 * Dumbed down version of the original, WIP DrawingLibraryInterface
 * TODO: replace this with the final one
 */
public interface DrawingLibraryInterface {

    /**
     * Export the currently drawn graph to the path using the specified format.
     * @param format The export format
     * @param path The path where the exported file should be saved
     */
    void export(String format, String path);

    /**
     * Returns all available formats for exporting.
     * @return Available formats in an array
     */
    String[] getAvailableExportFormats();

    /**
     * Returns the panel in which the graph is drawn.
     * @return A JComponent which draws the specified graphs
     */
    JComponent getPanel();

    /**
     * Set a new graph which should be drawn.
     * @param g The new graph
     */
    void setGraph(ListenableGraph<String, DefaultEdge> g);
    
}