package teo.isgci.drawing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.mxgraph.swing.mxGraphComponent;

class InternalMouseAdapter extends MouseAdapter {

    private mxGraphComponent graphComponent;

    /**
     * Constructor of the InternalMouseAdapter
     * @param graphComponent
     */
    protected InternalMouseAdapter(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;
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
            graphComponent.zoomIn();
        } else {
            graphComponent.zoomOut();
        }
    }
}