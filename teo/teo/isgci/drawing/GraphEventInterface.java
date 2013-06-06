package teo.isgci.drawing;

import java.awt.event.MouseAdapter;

/**
 * Dumbed down version of the original, WIP GraphEventInterface
 * TODO: replace this with the final one
 */
public interface GraphEventInterface {

    /**
     * Register a MouseAdapter to receive events from the graph panel.
     * @param adapter MouseAdapter
     */
    void registerMouseAdapter(MouseAdapter adapter);

}
