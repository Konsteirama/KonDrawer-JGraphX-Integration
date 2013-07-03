/*
 * Implementation of the GraphEventInterface.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */


package teo.isgci.drawing;

import java.awt.event.MouseAdapter;

import com.mxgraph.swing.mxGraphComponent;

/**
 * Implementation of the GraphEventInterface.
 */
class GraphEvent implements GraphEventInterface {

    /**
     * The actual canvas.
     */
    private mxGraphComponent graphComponent;

    /**
     * Gets a canvas and saves it.
     *
     * @param pGraphComponent : The canvas
     */
    protected GraphEvent(mxGraphComponent pGraphComponent) {
        this.graphComponent = pGraphComponent;
    }

    /**
     * Register a MouseAdapter to receive events from the graph panel.
     *
     * @param adapter MouseAdapter
     */
    @Override
    public void registerMouseAdapter(MouseAdapter adapter) {
        graphComponent.addMouseListener(adapter);
        graphComponent.getGraphControl().addMouseListener(adapter);
        graphComponent.addMouseWheelListener(adapter);
    }
}

/* EOF */
