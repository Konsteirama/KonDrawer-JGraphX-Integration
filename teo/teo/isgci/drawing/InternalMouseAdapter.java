/*
 * Internal mouse adapter for double and right click support.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.drawing;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Internal mouse adapter for double and right click support.
 */
class InternalMouseAdapter extends MouseAdapter {

    /** The parent object. */
    private GraphManipulation<?, ?> graphManipulation;
    
    /** the actual canvas. */
    private mxGraphComponent graphComponent;

    /** a boolean to handle the panning. */
    private boolean doPan;
    
    /**
     * Constructor of the InternalMouseAdapter.
     * 
     * @param pGraphComponent : The actual canvas
     * @param pGraphManipulation : The Interface to interact with the canvas
     */
    protected InternalMouseAdapter(mxGraphComponent pGraphComponent,
            GraphManipulation<?, ?> pGraphManipulation) {
        this.graphComponent = pGraphComponent;
        this.graphManipulation = pGraphManipulation;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        doPan = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            graphManipulation.zoom(true);
        } else {
            graphManipulation.zoom(false);
        }
    }
}

/* EOF */
