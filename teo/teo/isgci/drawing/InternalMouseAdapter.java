package teo.isgci.drawing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.mxgraph.swing.mxGraphComponent;

/**
 * Early version of original.
 * TODO replace with final version
 */
class InternalMouseAdapter extends MouseAdapter {

    private GraphManipulation<?, ?> graphManipulation;
    
    private mxGraphComponent graphComponent;

    /**
     * Constructor of the InternalMouseAdapter
     * 
     * @param graphComponent : The actual canvas
     * @param graphManipulation : The Interface to interact with the canvas
     */
    protected InternalMouseAdapter(mxGraphComponent graphComponent,
            GraphManipulation<?, ?> graphManipulation) {
        this.graphComponent = graphComponent;
        this.graphManipulation = graphManipulation;
    }

    private boolean doPan;

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
            graphManipulation.zoom(true, e.getPoint());
        } else {
            graphManipulation.zoom(false, e.getPoint());
        }
    }
}