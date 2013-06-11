package teo.isgci.drawing;

import org.jgrapht.Graph;

/**
 * Defines a static factory to create instances of a specific implementation
 * of the {@link #DrawingLibraryInterface}.
 * Uses the abstract factory pattern as well as the singleton pattern.
 * 
 * @param <V> The class of the vertices for the DrawingLibraryInterface
 * 
 * @param <E> The class of the edges for the DrawingLibraryInterface
 */
public abstract class DrawingLibraryFactory<V, E> {

    /**
     * The current instance of the implemented DrawingLibraryInterface.
     */
    private static DrawingLibraryFactory<?, ?> currentFactory;
    
    /**
     * Returns the current instance of the current
     * DrawingLibraryFactory implementation. 
     * 
     * @param <V> The type representing the vertices.
     * 
     * @param <E> The type representing the edges. 
     * 
     * @return
     *          The current instance of the DrawingLibraryFactory
     */
    public static <V, E> DrawingLibraryFactory<?, ?> getFactory() {
        if (currentFactory == null) {
            currentFactory = new JGraphXInterfaceFactory<V, E>();
        }
        
        return currentFactory;
    }
    
    /**
     * Creates and returns a new instance of a DrawingLibraryInterface.
     * 
     * @param graph
     *          The graph that will be handled by the DrawingLibraryInterface.
     * 
     * @return
     *          The generated instance of the DrawingLibraryInterface.
     */
    public abstract DrawingLibraryInterface<V, E> 
        createDrawingLibraryInterface(Graph<V, E> graph);
}
