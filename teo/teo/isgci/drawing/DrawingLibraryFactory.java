package teo.isgci.drawing;

import org.jgrapht.Graph;

/**
 * Defines a static factory to create instances of a specific implementation
 * of the {@link #DrawingLibraryInterface}.
 */
public abstract class DrawingLibraryFactory {

    /**
     * Creates a new specific implementation of the DrawingLibraryInterface.
     * 
     * @param graph
     *          The JGraphT graph which is passed to the drawinglibraryinterface
     * @return
     *          A specific implementation (currently: JGraphX) of the
     *          DrawingLibraryInterface
     */
    public static DrawingLibraryInterface createNewInterface(Graph graph) {
        return new JGraphXInterface(graph);
    }

}

