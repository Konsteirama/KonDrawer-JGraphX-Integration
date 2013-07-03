/*
 * Interface for the registration of mouse events.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */


package teo.isgci.drawing;

import java.awt.event.MouseAdapter;

/**
 * Interface for the registration of mouse events.
 */
public interface GraphEventInterface {

    /**
     * Register a MouseAdapter to receive events from the graph panel.
     *
     * @param adapter MouseAdapter
     */
    void registerMouseAdapter(MouseAdapter adapter);

}

/* EOF */
