package teo.isgci.drawing;

import java.awt.*;

/**
 * Dumbed down version of the original, WIP GraphEventInterface
 * TODO: replace this with the final one
 */
public interface GraphManipulationInterface {
    /**
     * returns a boolean, denoting whether the related graph is able to perform
     * a redo operation.
     *
     * @return
     */
    boolean canRedo();

    boolean canUndo();

    void centerNode(Object node);

    void colorNode(Object node, Color color);

    void markEdge(Object node1, Object node2);

    void reapplyHierarchicalLayout();

    void redo();

    void removeNode(Object node);

    void renameNode(Object node, String newName);

    void resetLayout();

    void undo();

    void zoom(double factor);

    void zoom(double factor, double centerx, double centery);
}
