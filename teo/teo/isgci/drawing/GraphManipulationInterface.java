package teo.isgci.drawing;

import java.awt.*;

/**
 * Dumbed down version of the original, WIP GraphEventInterface
 * TODO: replace this with the final one
 */
public interface GraphManipulationInterface<V, E> {
    /**
     * returns a boolean, denoting whether the related graph is able to perform
     * a redo operation.
     *
     * @return
     */
    boolean canRedo();

    boolean canUndo();

    void centerNode(V node);

    void colorNode(V[] node, Color color);

    void markEdge(E[] edges);
    
    void unmarkEdge(E[] edges);

    void reapplyHierarchicalLayout();

    void redo();

    void removeNode(V node);

    void renameNode(V node, String newName);

    void resetLayout();

    void undo();
    
    void zoomTo(double factor);

    void zoom(boolean zoomIn);

    void zoom(boolean zoomIn, Point center);

    void highlightNode(V node, boolean hightlightNeighbors);

    void unHiglightAll();
}
