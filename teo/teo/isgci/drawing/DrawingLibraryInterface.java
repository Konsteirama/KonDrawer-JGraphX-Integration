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

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.JComponent;

/**
 * Dumbed down version of the original, WIP DrawingLibraryInterface
 * TODO: replace this with the final one
 * @param <V>
 * @param <E>
 */
public interface DrawingLibraryInterface<V, E> {

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
     * Returns the Interface for registering events.
     * @return An instance of the GraphEventInterface
     */
    GraphEventInterface getGraphEventInterface();

    /**
     * Returns the interface for manipulating the shown graph.
     * @return An instance of the GraphManipulationInterface
     */
    GraphManipulationInterface getGraphManipulationInterface();

    /**
     * Returns the panel in which the graph is drawn.
     * @return A JComponent which draws the specified graphs
     */
    JComponent getPanel();

    /**
     * Set a new graph which should be drawn.
     * @param g The new graph
     */
    void setGraph(Graph<V, E> g);
    
}
