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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;

import teo.isgci.util.UserSettings;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

/**
 * This class implements the GraphManipulationInterface. It handles
 * manipulations that are done on the JGraphX-graph that is
 * viewed on the panel.
 * Manipulation will be done by the user.
 *
 * @param <V> : vertices of the graph
 * @param <E> : edges of the graph
 */
class GraphManipulation<V, E> implements GraphManipulationInterface<V, E> {

    /**
     * Defines the original edge color.
     */
    private static final Color EDGECOLOR = new Color(100, 130, 185);
    
    /**
     * Defines the color with which improper edges are marked.
     */
    private static final Color MARKEDCOLOR = Color.black;
    
    /**
     * How far the user can zoom in.
     */
    private static final double MAXZOOMLEVEL = 8;
    
    /**
     * Defines the thickness for highlighting.
     */
    private String cellThickness = "4";
    
    /**
     * How deep the children were highlighted.
     */
    private HashMap<mxICell, Integer> childHighlightingDepth
            = new HashMap<mxICell, Integer>();
    
    /**
     * The parent interface from which this object was created.
     */
    private JGraphXInterface<V, E> drawingLibrary;
    
    /**
     * Defines the color that should be used for highlighting.
     */
    private Color highlightColor;
    
    /**
     * Currently highlighted cells with their previous color.
     */
    private HashMap<mxICell, Color> highlightedCellsColor;
    
    /**
     * Currently highlighted cells with their previous thickness.
     */
    private HashMap<mxICell, String> highlightedCellsThickness;
    
    /**
     * How deep the parents were highlighted.
     */
    private HashMap<mxICell, Integer> parentHighlightingDepth
            = new HashMap<mxICell, Integer>();
    
    /**
     * Defines whether or not the undoHandler should record actions.
     */
    private int recordUndoableActions;
    
    /**
     * List of currently selected cells.
     */
    private List<mxICell> selectedCells;
    
    /**
     * Keeps track of which nodes were already visitied by 
     * {@link hasSignificantParents} and {@link hasSignificantChildren} to
     * prevent infinite loops.
     */
    private final List<V> visitedNodes = new ArrayList<V>();
    
    /**
     * Defines the color that should be used for selection.
     */
    private Color selectionColor;
    
    /**
     * Handles undo events in jgraphx.
     */
    private mxIEventListener undoHandler = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            if (recordUndoableActions == 0) {
                undoManager.undoableEditHappened((mxUndoableEdit) evt
                        .getProperty("edit"));
            }
        }
    };
    
    /**
     * Manages the undo-operations on the calling graph.
     */
    private mxUndoManager undoManager;
    
    /**
     * Handles showing the minimap on zoom or hiding it.
     */
    private ComponentListener zoomListener = new ComponentListener() {

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            applyZoomSettings();
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }
    };

    /**
     * Constructor of the class. Creates an instance of the GraphManipulation
     * class that operates on a given graphComponent from the given
     * JGraphXInterface.
     *
     * @param drawingLibraryInterface The drawingLibraryInterface from 
     *                                which this object originated
     */
    public GraphManipulation(
            JGraphXInterface<V, E> drawingLibraryInterface) {
        drawingLibrary = drawingLibraryInterface;
        mxGraphComponent graphComponent = drawingLibrary.getGraphComponent();


        // initialize colors
        highlightColor = UserSettings.getCurrentHighlightColor();
        selectionColor = UserSettings.getCurrentSelectionColor();

        // initiation of undoManager variable
        undoManager = new mxUndoManager();

        // notify undoManager about edits
        graphComponent.getGraph().getModel()
                .addListener(mxEvent.UNDO, undoHandler);
        graphComponent.getGraph().getView()
                .addListener(mxEvent.UNDO, undoHandler);

        // notify this component about size changes, to check if the minimap
        // has to be displayed/hidden
        graphComponent.addComponentListener(zoomListener);

        highlightedCellsColor = new HashMap<mxICell, Color>();
        highlightedCellsThickness = new HashMap<mxICell, String>();
        selectedCells = new ArrayList<mxICell>();
    }

    /**
     * Checks if the minimap should be hidden and hides it if necessary.
     * Sets the thickness according to zoom-level
     */
    public void applyZoomSettings() {
        // check minimap visibility
        mxGraphView view = drawingLibrary.getGraphComponent()
                .getGraph().getView();

        // Get the component and view heights/widths
        int compWidth = drawingLibrary.getGraphComponent().getWidth();
        int viewWidth = (int) view.getGraphBounds().getWidth();
        int compHeight = drawingLibrary.getGraphComponent().getHeight();
        int viewHeight = (int) view.getGraphBounds().getHeight();

        double relWidth = (double) compWidth / viewWidth;
        double relHeight = (double) compHeight / viewHeight;

        double scale;

        // If the relative Width is greater than the rel. height
        // set the scale to fit the width and vice versa
        if (relWidth <= relHeight) {
            scale = relWidth * view.getScale();
        } else {
            scale = relHeight * view.getScale();
        }

        if (getZoomLevel() > scale) {
            drawingLibrary.getGraphOutline().setVisible(true);
        } else {
            drawingLibrary.getGraphOutline().setVisible(false);
        }

        beginNotUndoable();
        // set thickness based on zoom
        if (getZoomLevel() < 0.4) {
            setThickness("12");
        } else if (getZoomLevel() < 0.7) {
            setThickness("8");
        } else {
            setThickness("4");
        }
        endNotUndoable();
        
        // Sets a mxGraphViewMargin
        drawingLibrary.getGraphComponent().getGraph().getView().setTranslate(
                new mxPoint(JGraphXInterface.MXGRAPHVIEW_MARGIN,
                        JGraphXInterface.MXGRAPHVIEW_MARGIN));
    }

    /**
     * Stops the recording of actions to the undo history.
     */
    @Override
    public void beginNotUndoable() {
        recordUndoableActions++;
    }

    /**
     * Call to start a block of updates.
     */
    @Override
    public void beginUpdate() {
        getGraphAdapter().getModel().beginUpdate();
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
        drawingLibrary.getGraphComponent().scrollCellToVisible(
                getCellFromNode(node), true);
    }

    @Override
    public void centerNodes(V[] nodes) {

        mxGraphComponent graphComponent = drawingLibrary.getGraphComponent();

        mxRectangle bounds = determineViewBounds(getCellsFromNodes(nodes),
                true);

        graphComponent.getGraphControl()
                .scrollRectToVisible(bounds.getRectangle());
    }

    @Override
    public void colorNode(V[] nodes, Color color) {

        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();

        beginUpdate();
        try {
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR,
                    mxUtils.hexString(color), getCellsFromNodes(nodes));
        } finally {
            endUpdate();
        }
    }

    /**
     * Starts the recording of actions to the undo history.
     */
    @Override
    public void endNotUndoable() {
        recordUndoableActions--;
    }

    /**
     * Call to end and execute a block of updates.
     */
    @Override
    public void endUpdate() {
        getGraphAdapter().getModel().endUpdate();
    }

    @Override
    public double getZoomLevel() {
        return drawingLibrary.getGraphComponent().getGraph()
                .getView().getScale();
    }

    @Override
    public void highlightChildren(List<V> roots) {
        beginNotUndoable();
        for (V root : roots) {
            mxICell mxRoot = getCellFromNode(root);

            childHighlightingDepth.put(mxRoot,
                    new Integer(childHighlightingDepth.get(mxRoot) + 1));
            highlightChildren(root, childHighlightingDepth.get(mxRoot));
        }
        endNotUndoable();

        /*
         * graphOutline sometimes won't take changes from this method, to
         * ensure that it properly shows all changes it's visibility is
         * turned off and on again.
         * FIXME graphOutline should react properly to this method
         */
        drawingLibrary.getGraphOutline().setVisible(false);
        applyZoomSettings();
    }

    @Override
    public void highlightParents(List<V> roots) {
        beginNotUndoable();
        for (V root : roots) {
            mxICell mxRoot = getCellFromNode(root);

            parentHighlightingDepth.put(mxRoot,
                    new Integer(parentHighlightingDepth.get(mxRoot) + 1));
            highlightParents(root, parentHighlightingDepth.get(mxRoot));
        }
        endNotUndoable();

        /*
         * graphOutline sometimes won't take changes from this method, to
         * ensure that it properly shows all changes it's visibility is
         * turned off and on again.
         * FIXME graphOutline should react properly to this method
         */
        drawingLibrary.getGraphOutline().setVisible(false);
        applyZoomSettings();
    }

    @Override
    public void markEdge(E[] edges) {
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();
        mxICell[] cells = getCellsFromEdges(edges);

        beginUpdate();
        try {
            for (mxICell edge : cells) {
                graph.setCellStyles(mxConstants.STYLE_STARTARROW,
                        mxConstants.ARROW_CLASSIC, new Object[]{edge});

                // update original values of highlighted cells
                if (highlightedCellsColor.containsKey(edge)) {
                    highlightedCellsColor.put(edge, MARKEDCOLOR);
                } else { // or update cell directly
                    graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                            mxUtils.hexString(MARKEDCOLOR),
                            new Object[]{edge});
                }
            }
        } finally {
            endUpdate();
        }
    }

    @Override
    public void reapplyHierarchicalLayout() {
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();

        beginUpdate();
        try {
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setFineTuning(true);
            layout.setInterHierarchySpacing(150);
            layout.setInterRankCellSpacing(100);
            layout.execute(graph.getDefaultParent());

        } finally {
            endUpdate();
        }

        applyZoomSettings();
    }

    @Override
    public void redo() {
        undoManager.redo();

        applyZoomSettings();
        
        // reapply highlighting completely
        unHighlightAll(false);
        reapplyHighlighting();
        updateSelectedCells();
    }

    @Override
    public void removeHighlightedNodes() {
        // get a copy of the list before removing everything
        List<mxICell> cells
                = new ArrayList<mxICell>(highlightedCellsColor
                        .keySet().size());

        for (mxICell cell : highlightedCellsColor.keySet()) {
            cells.add(cell);
        }

        // unhighlighting everything (thats why we need the copy)
        beginNotUndoable();
        unHighlightAll(false);
        endNotUndoable();

        // deselect all nodes
        drawingLibrary.setSelectedNodes(new ArrayList<V>());


        beginUpdate();
        try {
            HashMap<mxICell, V> cellToVertex
                    = getGraphAdapter().getCellToVertexMap();

            for (mxICell cell : cells) {
                if (cell.isVertex() && !cell.isCollapsed()) {
                    removeNode(cellToVertex.get(cell));
                }
            }

        } finally {
            endUpdate();
        }

    }

    @Override
    public void removeNode(V node) {       
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();

        unHighlightNode(node);

        beginUpdate();
        try {
            // Deletes every cell but leaves a small node for displaying
            // the edges
            mxICell cell = getCellFromNode(node);

            // move cells so the tiny cells are still in the middle
            graph.moveCells(new Object[] { cell }, cell.getGeometry()
                    .getWidth() / 2, cell.getGeometry().getHeight() / 2);

            // make cells nearly invisible
            cell.getGeometry().setAlternateBounds(new mxRectangle(0, 0, 0, 0));
            
            // set label to one space to make it undoable
            graph.getModel().setValue(cell, " ");

            // fold cells so only edges are still visible
            graph.cellsFolded(new Object[] { cell }, true, false);
            
            // diable cell movement
            graph.setCellStyles(mxConstants.STYLE_MOVABLE, "0", 
                    new Object[] { cell });
            
            // remove all cells without "visible" parents/children
            removeLooseNodes(cell, true, true);
            
        } finally {
            endUpdate();
        }

        applyZoomSettings();
        updateSelectedCells();
    }

    @Override
    public void renameNode(V node, String newName) {
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();

        mxICell cell = getCellFromNode(node);

        graph.getModel().setValue(cell, newName);
        graph.updateCellSize(cell);

        applyZoomSettings();
    }

    @Override
    public void setBackgroundColor(Color color) {
        beginUpdate();
        try {
            drawingLibrary.getGraphComponent()
                    .getViewport().setBackground(color);
        } finally {
            endUpdate();
        }
    }

    @Override
    public void setFontColor(Color color) {

        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();

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
    public void setHighlightColor(Color color) {
        highlightColor = color;

        for (mxICell cell : highlightedCellsColor.keySet()) {

            // ignore selected cells
            if (selectedCells.contains(cell)) {
                continue;
            }

            getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                    mxUtils.getHexColorString(color), new Object[]{cell});
        }
    }

    @Override
    public void setSelectionColor(Color color) {
        selectionColor = color;

        for (mxICell cell : selectedCells) {
            getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                    mxUtils.getHexColorString(color), new Object[]{cell});
        }
    }

    @Override
    public void unHighlightAll() {
        unHighlightAll(true);
    }

    @Override
    public void unHighlightNode(V tnode) {
        mxICell node = getCellFromNode(tnode);
        parentHighlightingDepth.remove(node);
        childHighlightingDepth.remove(node);

        beginNotUndoable();
        unHighlightAll(false);
        reapplyHighlighting();
        endNotUndoable();

        /*
         * graphOutline sometimes won't take changes from this method, to
         * ensure that it properly shows all changes it's visibility is
         * turned off and on again.
         * FIXME graphOutline should react properly to this method
         */
        drawingLibrary.getGraphOutline().setVisible(false);
        applyZoomSettings();
    }

    @Override
    public void undo() {
        undoManager.undo();
        applyZoomSettings();
        
        // reapply highlighting completely
        unHighlightAll(false);
        reapplyHighlighting();
        updateSelectedCells();
    }

    @Override
    public void unmarkEdge(E[] edges) {
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();
        mxICell[] cells = getCellsFromEdges(edges);


        beginUpdate();
        try {
            for (mxICell edge : cells) {

                graph.setCellStyles(mxConstants.STYLE_STARTARROW, "",
                        new Object[]{edge});

                // update original values of highlighted cells and
                if (highlightedCellsColor.containsKey(edge)) {
                    highlightedCellsColor.put(edge, EDGECOLOR);
                } else { // or update cell directly
                    graph.setCellStyles(mxConstants.STYLE_STROKECOLOR,
                            mxUtils.hexString(EDGECOLOR),
                            new Object[]{edge});
                }
            }
        } finally {
            endUpdate();
        }
    }

    /**
     * Unregisters all eventlisteners.
     */
    public void unregisterEventListener() {
        drawingLibrary.getGraphComponent().getGraph().getModel()
                .removeListener(undoHandler, mxEvent.UNDO);
        drawingLibrary.getGraphComponent().getGraph().getView().removeListener(
                undoHandler, mxEvent.UNDO);

        drawingLibrary.getGraphComponent()
                .removeComponentListener(zoomListener);
    }

    /**
     * Updates all selected cells and adds a border around them.
     */
    public void updateSelectedCells() {
        beginNotUndoable();

        // build difference between old cells and new cells
        Object[] newCells
                = drawingLibrary.getGraphComponent()
                        .getGraph().getSelectionCells();

        // convert new cells to list
        List<mxICell> currentCells = new ArrayList<mxICell>(newCells.length);

        for (Object obj : newCells) {
            if (obj instanceof mxICell) {
                currentCells.add((mxICell) obj);
            }
        }

        // get difference of nodes that are no longer selected
        for (mxICell cell : selectedCells) {
            if (!currentCells.contains(cell)) {
                parentHighlightingDepth.remove(cell);
                childHighlightingDepth.remove(cell);
            }
        }

        // unhighlight all, but reapply highlighting later again
        unHighlightAll(false);

        selectedCells.clear();

        //  rebuild selection for all cells
        for (mxICell cell : currentCells) {
            selectedCells.add(cell);
            highlightCell(cell, selectionColor);

            if (!parentHighlightingDepth.containsKey(cell)) {
                parentHighlightingDepth.put(cell, new Integer(0));
                childHighlightingDepth.put(cell, new Integer(0));
            }
        }

        if (newCells.length == 0) {
            parentHighlightingDepth.clear();
            childHighlightingDepth.clear();
        }

        reapplyHighlighting();
        endNotUndoable();
    }

    @Override
    public void zoom(boolean zoomIn) {
        drawingLibrary.getGraphComponent().setCenterZoom(true);

        if (zoomIn) {
            if (getZoomLevel() < MAXZOOMLEVEL) {
                drawingLibrary.getGraphComponent().zoomIn();
            }
        } else {
            drawingLibrary.getGraphComponent().zoomOut();
        }

        applyZoomSettings();
    }

    @Override
    public void zoomTo(double factor) {
        factor = Math.min(MAXZOOMLEVEL, factor);

        drawingLibrary.getGraphComponent().zoomTo(factor, true);

        applyZoomSettings();
    }

    @Override
    public void zoomToFit() {
        mxGraphView view = drawingLibrary.getGraphComponent()
                .getGraph().getView();

        // Get the component and view heights/widths
        int compWidth = drawingLibrary.getGraphComponent().getWidth();
        int viewWidth = (int) view.getGraphBounds().getWidth();
        int compHeight = drawingLibrary.getGraphComponent().getHeight();
        int viewHeight = (int) view.getGraphBounds().getHeight();

        double relWidth = (double) compWidth / viewWidth;
        double relHeight = (double) compHeight / viewHeight;

        // If the relative Width is greater than the rel. height
        // set the scale to fit the width and vice versa
        if (relWidth <= relHeight) {
            view.setScale(relWidth * view.getScale());
        } else {
            view.setScale(relHeight * view.getScale());
        }

        applyZoomSettings();
    }

    /**
     * Determines the viewBounds and returns it in a mxRectangle.
     * @param cells
     *          The cells which should be in view
     * @param center
     *          Whether to center the view
     * @return
     *          A mxRectangle where the view should be
     */
    private mxRectangle determineViewBounds(mxICell[] cells, boolean center) {

        mxGraphComponent graphComponent = drawingLibrary.getGraphComponent();

        if (cells == null || cells.length == 0) {
            return null;
        }

        mxRectangle rect = null;

        for (mxICell cell : cells) {
            if (rect == null) {
                rect = getGraphAdapter().getCellBounds(cell);
            } else {
                rect.add(getGraphAdapter().getCellBounds(cell));
            }
        }

        if (center) {

            rect = (mxRectangle) rect.clone();

            rect.setX(rect.getCenterX() - graphComponent.getWidth() / 2);
            rect.setWidth(graphComponent.getWidth());
            rect.setY(rect.getCenterY() - graphComponent.getHeight() / 2);
            rect.setHeight(graphComponent.getHeight());
        }
        return rect;
    }

    /**
     * Returns the cell associated with the given edge.
     *
     * @param edge The edge for which the cell should be returned
     * @return The cell associated with the edge
     */
    private mxICell getCellFromEdge(E edge) {
        return getGraphAdapter().getEdgeToCellMap().get(edge);
    }

    /**
     * Returns the cell associated with the given node.
     *
     * @param node The node for which the cell should be returned
     * @return The cell associated with the node
     */
    private mxICell getCellFromNode(V node) {
        return getGraphAdapter().getVertexToCellMap().get(node);
    }

    /**
     * Returns the cells associated with the given edges.
     *
     * @param edges The edges for which the cells should be returned
     * @return The cells associated with the edges
     */
    private mxICell[] getCellsFromEdges(E[] edges) {
        mxICell[] cells = new mxICell[edges.length];
        for (int i = 0; i < edges.length; i++) {
            cells[i] = getCellFromEdge(edges[i]);
        }
        return cells;
    }

    /**
     * Returns the cells associated with the given nodes.
     *
     * @param nodes The nodes for which the cells should be returned
     * @return The cells associated with the nodes
     */
    private mxICell[] getCellsFromNodes(V[] nodes) {
        mxICell[] cells = new mxICell[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            cells[i] = getCellFromNode(nodes[i]);
        }
        return cells;
    }

    /**
     * Returns the current graph adapter.
     *
     * @return The current graph adapter
     */
    private JGraphXAdapter<V, E> getGraphAdapter() {
        return (JGraphXAdapter<V, E>) drawingLibrary
                .getGraphComponent().getGraph();
    }

    /**
     * Highlights a cell with the given color and thickness.
     *
     * @param cell  The cell to highlight.
     * @param color The color in which the node should be highlighted
     */
    private void highlightCell(mxICell cell, Color color) {

        // don't overwrite original values
        if (!highlightedCellsColor.containsKey(cell)) {

            // save old values
            highlightedCellsThickness.put(cell, mxUtils.getString(
                    getGraphAdapter().getCellStyle(cell),
                    mxConstants.STYLE_STROKEWIDTH));

            highlightedCellsColor.put(cell, mxUtils.getColor(getGraphAdapter()
                    .getCellStyle(cell), mxConstants.STYLE_STROKECOLOR));
        }

        beginNotUndoable();

        // apply new values
        getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                mxUtils.getHexColorString(color),
                new Object[]{cell});
        getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKEWIDTH,
                cellThickness, new Object[]{cell});

        endNotUndoable();
    }

    /**
     * Highlights the children of a node to a certain depth.
     *
     * @param root  The root where to begin highlighting
     * @param depth How deep the highlighting should go
     */
    private void highlightChildren(V root, int depth) {
        Graph<V, E> graph = drawingLibrary.getGraph();
        Set<E> edges = graph.edgesOf(root);

        // don't highlight selected cells
        if (!selectedCells.contains(getCellFromNode(root))) {
            highlightCell(getCellFromNode(root), highlightColor);
        }

        // recursion end
        if (depth == 0) {
            return;
        }

        for (E edge : edges) {
            V child = graph.getEdgeTarget(edge);

            // edge was pointing to parent
            if (child == root) {
                continue;
            }

            // highlight edge
            highlightCell(getCellFromEdge(edge), highlightColor);

            // recursively highlight children until depth = 0
            highlightChildren(child, depth - 1);
        }

    }

    /**
     * Highlights the parents of a node to a certain depth.
     *
     * @param root  The root where to begin highlighting
     * @param depth How deep the highlighting should go
     */
    private void highlightParents(V root, int depth) {
        Graph<V, E> graph = drawingLibrary.getGraph();
        Set<E> edges = graph.edgesOf(root);

        // don't highlight selected cells
        if (!selectedCells.contains(getCellFromNode(root))) {
            highlightCell(getCellFromNode(root), highlightColor);
        }

        // recursion end
        if (depth == 0) {
            return;
        }

        for (E edge : edges) {
            V parent = graph.getEdgeSource(edge);

            // edge was pointing to child
            if (parent == root) {
                continue;
            }

            // highlight edge
            highlightCell(getCellFromEdge(edge), highlightColor);

            // recursively highlight children until depth = 0
            highlightParents(parent, depth - 1);
        }

    }

    /**
     * Restores highlighting with the current depth.
     */
    private void reapplyHighlighting() {
        HashMap<mxICell, V> cellToNode
                = getGraphAdapter().getCellToVertexMap();

        for (mxICell cell : parentHighlightingDepth.keySet()) {
            highlightParents(cellToNode.get(cell),
                    parentHighlightingDepth.get(cell));
        }

        for (mxICell cell : childHighlightingDepth.keySet()) {
            highlightChildren(cellToNode.get(cell),
                    childHighlightingDepth.get(cell));
        }
    }

    /**
     * Removes a node from the canvas.
     * 
     * @param node
     *            The node that should be removed.
     */
    private void deleteNode(mxICell node) {
        mxGraph graph = drawingLibrary.getGraphComponent().getGraph();
        Object[] cells = new Object[]{node};
    
        // Adds all edges connected to the node
        cells = graph.addAllEdges(cells);
    
        // remove
        for (Object object : cells) {
            graph.getModel().remove(object);
        }
    }

    /** 
     * Removes loose nodes. "Loose" is refering to nodes that have been
     * collapsed via {@link removeNode} and have no visible parents/children.
     * 
     * @param node
     *          Where to begin
     * @param removeParents
     *          Whether to recurse into parentnodes
     * @param removeChildren
     *          Whether to recurse into childnodes
     */
    private void removeLooseNodes(mxICell node, 
                                  boolean removeParents, 
                                  boolean removeChildren) {
        V tnode = getGraphAdapter().getCellToVertexMap().get(node);
        
        visitedNodes.clear();
        boolean hasSignificantChildren = hasSignificantChildren(tnode);
        visitedNodes.clear();
        boolean hasSignificantParents = hasSignificantParents(tnode);
        
        if (hasSignificantChildren && hasSignificantParents) {
            return;
        }
        
        deleteNode(node);

        if (removeParents) {
            List<V> parents = getNodeParents(tnode);

            for (V parent : parents) {
                if (getCellFromNode(parent).isCollapsed()) {
                    removeLooseNodes(getCellFromNode(parent), true, false);
                }
            }
        }

        if (removeChildren) {
            List<V> children = getNodeChildren(tnode);

            for (V child : children) {
                if (getCellFromNode(child).isCollapsed()) {
                    removeLooseNodes(getCellFromNode(child), false, true);
                }
            }
        }

    }
    
    /**
     * Checks whether the given node has significant children.
     * Significant means: - The node has children
     *                    - These children are neither collapsed nor deleted
     * 
     * @param start
     *          Where to start searching
     * @return
     *          Whether the node has significant children.
     */
    private boolean hasSignificantChildren(V start) {
        // prevent endless circles
        if (visitedNodes.contains(start)) {
            return false;
        }
        
        visitedNodes.add(start);

        List<V> children = getNodeChildren(start);
        ArrayList<mxICell> mxChildren 
            = new ArrayList<mxICell>(children.size());

        // fill mxList
        for (V child : children) {
            mxChildren.add(getCellFromNode(child));
        }    
            
        // check all nodes first if one was found already
        for (mxICell cell : mxChildren) {
            if (!cell.isCollapsed() && cell.isVisible()) {
                return true;
            }
        }
        
        // check all children
        for (V child : children) {
            if (hasSignificantChildren(child)) {
                return true;
            }
        }
        
        // nothing found
        return false;
    }
    
    /**
     * Checks whether the given node has significant parents.
     * Significant means: - The node has parents
     *                    - These parents are neither collapsed nor deleted 
     * 
     * @param start
     *          Where to start searching
     * @return
     *          Whether the node has significant parents.
     */
    private boolean hasSignificantParents(V start) {
        // prevent endless circles
        if (visitedNodes.contains(start)) {
            return false;
        }
        
        visitedNodes.add(start);

        List<V> parents = getNodeParents(start);
        ArrayList<mxICell> mxParents = new ArrayList<mxICell>(parents.size());
        
        // fill mxList
        for (V parent : parents) {
            mxParents.add(getCellFromNode(parent));
        }
        
        // check all nodes first if one was found already
        for (mxICell cell : mxParents) {
            if (!cell.isCollapsed() && cell.isVisible()) {
                return true;
            }
        }
        
        // check all parents
        for (V parent : parents) {
            if (hasSignificantParents(parent)) {
                return true;
            }
        }
        
        // nothing found
        return false;
    }
    
    /**
     * Collects all parents of a jgrapht node.
     * @param node
     *          The node which parents are returned.
     * @return
     *          The parents of the given node.
     */
    private List<V> getNodeParents(V node) {
        Graph<V, E> graph = drawingLibrary.getGraph();
        Set<E> edges = graph.edgesOf(node);
        
        ArrayList<V> parents = new ArrayList<V>();
        
        // gather all parents/children
        for (E edge : edges) {
            V parent = graph.getEdgeSource(edge);
            
            if (parent != node) {
                parents.add(parent);
            }
        }
        
        return parents;
    }

    /**
     * Collects all children of a jgrapht node.
     * @param node
     *          The node which the children are returned.
     * @return
     *          The children of the given node.
     */
    private List<V> getNodeChildren(V node) {
        Graph<V, E> graph = drawingLibrary.getGraph();
        Set<E> edges = graph.edgesOf(node);
        
        ArrayList<V> children = new ArrayList<V>();
        
        // gather all parents/children
        for (E edge : edges) {
            V child = graph.getEdgeTarget(edge);
            
            if (child != node) {
                children.add(child);
            }
        }
        
        return children;
    }
    
    /**
     * Sets the thickness of highlighting and selection.
     *
     * @param value The thickness that should be applied.
     */
    private void setThickness(String value) {
        // nothing to do
        if (cellThickness.equals(value)) {
            return;
        }

        cellThickness = value;

        for (mxICell cell : highlightedCellsThickness.keySet()) {
            getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKEWIDTH,
                    value, new Object[]{cell});
        }
    }

    /**
     * Unhighlights all nodes.
     *
     * @param clear Determines whether or not highlighting depth 
     *              should be deleted.
     *              Use false if this method is followed by reapplyHighlighting
     */
    private void unHighlightAll(boolean clear) {
        beginNotUndoable();
        beginUpdate();
        try {
            for (mxICell cell : highlightedCellsColor.keySet()) {

                getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKECOLOR,
                        mxUtils.getHexColorString(
                                highlightedCellsColor.get(cell)),
                        new Object[]{cell});

                getGraphAdapter().setCellStyles(mxConstants.STYLE_STROKEWIDTH,
                        highlightedCellsThickness.get(cell),
                        new Object[]{cell});

            }

            highlightedCellsColor.clear();
            highlightedCellsThickness.clear();

        } finally {
            endUpdate();
            endNotUndoable();
            
            /*
             * graphOutline sometimes won't take changes from this method, to
             * ensure that it properly shows all changes it's visibility is 
             * turned off and on again.
             * FIXME graphOutline should react properly to this method
             */
            drawingLibrary.getGraphOutline().setVisible(false);
            applyZoomSettings();
        }

        if (clear) {
            parentHighlightingDepth.clear();
            childHighlightingDepth.clear();
            selectedCells.clear();
        }
    }
}

/* EOF */
