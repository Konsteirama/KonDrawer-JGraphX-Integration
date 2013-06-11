package teo.isgci.drawing;

import org.jgrapht.Graph;

/**
 * Used to generate new instances of the JGraphXInterface. Uses the 
 * abstract factory software pattern as well as the singleton pattern.
 * 
 * @param <V>
 * 
 * @param <E>
 */
public class JGraphXInterfaceFactory<V, E> extends DrawingLibraryFactory<V, E> {
    
    /**
     * Should only be used by the DrawingLibraryFactory.
     * Use {@link #getFactory()} to get a new instance of this type.
     */
    public JGraphXInterfaceFactory() {
        // nothing to do
    }
    
    @Override
    public DrawingLibraryInterface<V, E> createDrawingLibraryInterface(
            Graph<V, E> graph) {
        return new JGraphXInterface<V, E>(graph);
    }

}
