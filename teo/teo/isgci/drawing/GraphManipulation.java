/*
 * This class implements the GraphManipulationInterface. It handles
 * manipulations that are done on the JGraphX-graph that
 * is viewed on the panel.
 * Manipulation will be done by the user.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.drawing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graph;

import teo.isgci.util.Latex2Html;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import com.mxgraph.view.mxStylesheet;

/**
 * This class implements the GraphManipulationInterface. It handles
 * manipulations that are done on the JGraphX-graph that
 * is viewed on the panel.
 * Manipulation will be done by the user.
 *
 * @param <V> : vertices of the graph
 * @param <E> : edges of the graph
 */
class GraphManipulation<V, E> implements GraphManipulationInterface<V, E> {

    /** Defines the color that should be used for highlighting. */
    private static final Color HILIGHTCOLOR = new Color(0xFF, 0xAA, 0x00);
    
    /** Defines the thickness for highlighting. */
    private static final String HILIGHTTHICKNESS = "2";

    private boolean recordUndoableActions = true;
    
    protected mxIEventListener undoHandler = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            if(recordUndoableActions)
            {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
            }
        }
    };


    /**
     * GraphComponent is the panel the graph is drawn in.
     */
    private mxGraphComponent graphComponent;
    
    /**
     * Manages the undo-operations on the calling graph.
     */
    private mxUndoManager undoManager;
    
    /**
     * Currently highlighted cells with their previous color.
     */
    private HashMap<mxICell, Color> markedCellsColor;
    
    /**
     * Currently highlighted cells with their previous thickness.
     */
    private HashMap<mxICell, String> markedCellsThickness;

    /**
     * Constructor of the class. Creates an instance of the GraphManipulation
     * class that operates on a given graphComponent.
     *
     * @param pGraphComponent : a JGRaphX graphComponent, shown on the panel
     */
    public GraphManipulation(mxGraphComponent pGraphComponent) {
        this.graphComponent = pGraphComponent;

        // initiation of undoManager variable
        this.undoManager = new mxUndoManager();

        //notify undoManager about edits
        graphComponent.getGraph().getModel().
                addListener(mxEvent.UNDO, undoHandler);
        graphComponent.getGraph().getView().
                addListener(mxEvent.UNDO, undoHandler);

        markedCellsColor = new HashMap<mxICell, Color>();
        markedCellsThickness = new HashMap<mxICell, String>();
    }

    /**
     * Returns the current graph adapter.
     *
     * @return The current graph adapter
     */
    @SuppressWarnings("unchecked")
    private JGraphXAdapter<V, E> getGraphAdapter() {
        return (JGraphXAdapter<V, E>) graphComponent.getGraph();
    }

    /**
     * Returns the cell associated with the given node.
     *
     * @param node
     * @return
     */
    private mxICell getCellFromNode(V node) {
        return getGraphAdapter().getVertexToCellMap().get(node);
    }

    /**
     * Returns the cell associated with the given edge.
     *
     * @param edge
     * @return
     */
    private mxICell getCellFromEdge(E edge) {
        return getGraphAdapter().getEdgeToCellMap().get(edge);
    }

    /**
     * Returns the cells associated with the given nodes.
     *
     * @param nodes
     * @return
     */
    private mxICell[] getCellsFromNodes(V[] nodes) {
        mxICell[] cells = new mxICell[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            cells[i] = getCellFromNode(nodes[i]);
        }
        return cells;
    }

    /**
     * Returns the cells associated with the given edges.
     *
     * @param edges
     * @return
     */
    private mxICell[] getCellsFromEdges(E[] edges) {
        mxICell[] cells = new mxICell[edges.length];
        for (int i = 0; i < edges.length; i++) {
            cells[i] = getCellFromEdge(edges[i]);
        }
        return cells;
    }

    @Override
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    @Override
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    @Override
    public void centerNode(V node) {
            graphComponent.scrollCellToVisible(getCellFromNode(node), true);
    }

    @Override
    public void colorNode(V[] nodes, Color color) {

        mxGraph graph = graphComponent.getGraph();

        beginUpdate();
        try {
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,
                    mxUtils.hexString(color), getCellsFromNodes(nodes));
        } finally {
            endUpdate();
        }
    }
    
    @Override
    public void setFontColor(Color color) {
        
        mxGraph graph = graphComponent.getGraph();
        
        beginUpdate();
        try {
            
            graph.setCellStyles(mxConstants.STYLE_FONTCOLOR, 
                    mxUtils.hexString(color), 
                    graph.getChildVertices(graph.getDefaultParent()));
            
        } finally {
            endUpdate();
        }
    }
    
    @Override
    public void setBackgroundColor(Color color) {
        beginUpdate();
        try {
            graphComponent.getViewport().setBackground(color);
        } finally {
            endUpdate();
        }
    }

    @Override
    public void markEdge(E[] edges) {

        mxGraph graph = graphComponent.getGraph();

        beginUpdate();
        try {
            graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                    mxUtils.hexString(Color.black), getCellsFromEdges(edges));
        } finally {
            endUpdate();
        }
    }

    @Override
    public void unmarkEdge(E[] edges) {
        mxGraph graph = graphComponent.getGraph();

        beginUpdate();
        try {
            graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                    mxUtils.hexString(new Color(100, 130, 185)),
                    getCellsFromEdges(edges));

        } finally {
            endUpdate();
        }
    }

    @Override
    public void reapplyHierarchicalLayout() {
        mxGraph graph = graphComponent.getGraph();

        beginUpdate();
        try {
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.execute(graph.getDefaultParent());

        } finally {
            endUpdate();
        }
    }

    @Override
    public void redo() {
        undoManager.redo();
    }

    @Override
    public void removeNode(V node) {
        mxGraph graph = graphComponent.getGraph();

        Object[] cells = new Object[]{getCellFromNode(node)};

        // Adds all edges connected to the node
        cells = graph.addAllEdges(cells);

        beginUpdate();
        try {
            // Deletes every cell
            for (Object object : cells) {
                graph.getModel().remove(object);
            }
        } finally {
            endUpdate();
        }
    }

    @Override
    public void highlightNode(V node, boolean hightlightNeighbors) {

        ArrayList<mxICell> cells = new ArrayList<mxICell>(1);
        mxICell cell = getCellFromNode(node);
        cells.add(cell);

        if (!markedCellsColor.containsKey(cell)) {
            markedCellsThickness.put(cell, 
                    mxUtils.getString(getGraphAdapter().
                            getCellStyle(cell), 
                            mxConstants.STYLE_STROKEWIDTH));
            
            markedCellsColor.put(cell, mxUtils.getColor(getGraphAdapter()
                    .getCellStyle(cell), mxConstants.STYLE_STROKECOLOR));
        }
        
        if (hightlightNeighbors) {
            for (int i = 0; i < cell.getEdgeCount(); i++) {
                mxCell edge = (mxCell) cell.getEdgeAt(i);
                markedCellsColor.put(edge, mxUtils.getColor(getGraphAdapter()
                        .getCellStyle(edge), mxConstants.STYLE_STROKECOLOR));

                markedCellsThickness.put(edge, 
                        mxUtils.getString(getGraphAdapter().
                                getCellStyle(edge), 
                                mxConstants.STYLE_STROKEWIDTH));
                
                cells.add(edge);
                
                mxICell source = edge.getSource();
                mxICell target = edge.getTarget();

                if (!markedCellsColor.containsKey(source)) {
                    markedCellsThickness.put(source, 
                            mxUtils.getString(getGraphAdapter().
                                    getCellStyle(source), 
                                    mxConstants.STYLE_STROKEWIDTH));
                    
                    markedCellsColor.put(source, 
                            mxUtils.getColor(getGraphAdapter()
                                    .getCellStyle(source),
                            mxConstants.STYLE_STROKECOLOR));
                    cells.add(source);
                }

                if (!markedCellsColor.containsKey(target)) {
                    
                    markedCellsThickness.put(target, 
                            mxUtils.getString(getGraphAdapter().
                                    getCellStyle(target), 
                                    mxConstants.STYLE_STROKEWIDTH));
                    
                    markedCellsColor.put(target, mxUtils.getColor(
                            getGraphAdapter().getCellStyle(target),
                            mxConstants.STYLE_STROKECOLOR));
                    cells.add(target);
                }
            }
        }

        beginUpdate();
        try {
            getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                    mxUtils.getHexColorString(HILIGHTCOLOR), cells.toArray());
            getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKEWIDTH,
                    HILIGHTTHICKNESS, cells.toArray());
        } finally {
            endUpdate();
        }
    }

    /**
     * Starts the recording of actions to the undo history.
     */
    @Override
    public void endNotUndoable() {
        recordUndoableActions = true;
    }

    /**
     * Stops the recording of actions to the undo history.
     */
    @Override
    public void beginNotUndoable() {
        recordUndoableActions = false;
    }

    @Override
    public void unHiglightAll() {
        beginUpdate();
        try {
            for (mxICell cell : markedCellsColor.keySet()) {
                getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKEWIDTH,
                        markedCellsThickness.get(cell), new Object[]{cell});
                
                getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                        mxUtils.getHexColorString(markedCellsColor.get(cell)),
                        new Object[]{cell});
            }
        } finally {
            endUpdate();
            markedCellsColor.clear();
            markedCellsThickness.clear();
        }
    }

    /**
     * Call to start a block of updates.
     */
    @Override
    public void beginUpdate() {
        getGraphAdapter().getModel().beginUpdate();
    }

    /**
     * Call to end and execute a block of updates.
     */
    @Override
    public void endUpdate() {
        getGraphAdapter().getModel().endUpdate();
    }

    @Override
    public void renameNode(V node, String newName) {
        mxGraph graph = graphComponent.getGraph();

        newName = Latex2Html.getInstance().html(newName);

        mxICell cell = getCellFromNode(node);

        graph.getModel().setValue(cell, newName);
        graph.updateCellSize(cell);
    }

    @Override
    public void resetLayout() {
        Graph<V, E> graphT = getGraphAdapter().getGraph();

        JGraphXAdapter<V, E> newGraphAdapter 
            = new JGraphXAdapter<V, E>(graphT);
    }

    @Override
    public void undo() {
        undoManager.undo();
    }

    @Override
    public void zoomTo(double factor) {
        graphComponent.zoomTo(factor, true);
    }

    @Override
    public void zoom(boolean zoomIn) {
        graphComponent.setCenterZoom(true);

        if (zoomIn) {
            graphComponent.zoomIn();
        } else {
            graphComponent.zoomOut();
        }
    }

    @Override
    public void zoomToFit() {
        mxGraphView view = graphComponent.getGraph().getView();

        int compLen = graphComponent.getWidth();
        int viewLen = (int) view.getGraphBounds().getWidth();

        view.setScale((double) compLen / viewLen * view.getScale());
    }
}

/* EOF */
