/*
 * Replace this line with a (multi-line) description of this file...
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.drawing;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * Dumbed down version of original, WIP JGraphXAdapter
 * TODO: replace with final one
 * 
 * @author Base: JeanYves Tinevez
 * @author Improvements: KonSteiRaMa
 *
 * @param <V> Vertex
 * @param <E> Edge
 */
class JGraphXAdapter<V, E> extends mxGraph implements GraphListener<V, E> {

        /**
         * The graph to be drawn.
         * Has vertices "V" and edges "E".
         */
    private Graph<V, E> graphT;

    /**
     * Maps the JGraphT-Vertices onto JGraphX-mxCells.
     * {@link #cellToVertexMap} is for the opposite direction.
     */
    private HashMap<V, mxCell> vertexToCellMap = new HashMap<V, mxCell>();

    /**
     * Maps the JGraphT-Edges onto JGraphX-mxCells.
     * {@link #cellToEdgeMap} is for the opposite direction.
     */
    private HashMap<E, mxCell> edgeToCellMap = new HashMap<E, mxCell>();

    /**
     * Maps the JGraphX-mxCells onto JGraphT-Edges.
     * {@link #edgeToCellMap} is for the opposite direction.
     */
    private HashMap<mxCell, V> cellToVertexMap = new HashMap<mxCell, V>();

    /**
     * Maps the JGraphX-mxCells onto JGraphT-Vertices.
     * {@link #vertexToCellMap} is for the opposite direction.
     */
    private HashMap<mxCell, E> cellToEdgeMap = new HashMap<mxCell, E>();

    
    
    
    //                        Constructors
    // ================================================================
    
    /**
     * Constructs and draws a new ListenableGraph. If the graph changes
     * through as ListenableGraph, the JGraphXAdapter will automatically
     * add/remove the new edge/vertex as it implements the GraphListener
     * interface.
     * Throws a IllegalArgumentException if the graph is null.
     * @param graph casted to graph
     */
    public JGraphXAdapter(final ListenableGraph<V, E> graph) {
        // call normal constructor with graph class
        this((Graph<V, E>) graph);
        
        graph.addGraphListener(this);
    }
    
    /**
     * Constructs and draws a new mxGraph from a jGraphT graph. Changes on 
     * the jgraphT graph will not edit this mxGraph any further; use the 
     * constructor with the ListenableGraph parameter instead or use this 
     * graph as a normal mxGraph.
     * 
     * Throws an IllegalArgumentException if the parameter is null.
     * @param graph is a graph
     */
    public JGraphXAdapter(final Graph<V, E> graph) {
        super();
        
        // Don't accept null as jgrapht graph
        if (graph == null) {
            throw new IllegalArgumentException();
        } else {
            this.graphT = graph;
        }
        
        // generate the drawing
        insertJGraphT(graph);
        
        setAutoSizeCells(true);
    }


    
   
    //                            Getter
    // ================================================================
    
    /**
     * Returns Hashmap which maps the vertices onto their visualization mxCells.
     * @return {@link #vertexToCellMap}
     */
    public final HashMap<V, mxCell> getVertexToCellMap() {
        return vertexToCellMap;
    }

    /**
     * Returns Hashmap which maps the edges onto their visualization mxCells.
     * @return {@link #edgeToCellMap}
     */
    public final HashMap<E, mxCell> getEdgeToCellMap() {
        return edgeToCellMap;
    }

    /**
     * Returns Hashmap which maps the visualization mxCells onto their edges.
     * @return {@link #cellToEdgeMap}
     */
    public final HashMap<mxCell, E> getCellToEdgeMap() {
        return cellToEdgeMap;
    }

    /**
     * Returns Hashmap which maps the visualization mxCells onto their vertices.
     * @return {@link #cellToVertexMap}
     */
    public final HashMap<mxCell, V> getCellToVertexMap() {
        return cellToVertexMap;
    }
    
    /**
     * blabla doc.
     * @return {@link #graphT}
     */
    public final Graph<V, E> getGraph() {
        return graphT;
    }
    

    //                     GraphListener Interface
    // ================================================================
    
    @Override
    public final void vertexAdded(final GraphVertexChangeEvent<V> e) {
        addJGraphTVertex(e.getVertex());
    }

    @Override
    public final void vertexRemoved(final GraphVertexChangeEvent<V> e) {
        mxCell cell = vertexToCellMap.remove(e.getVertex());
        removeCells(new Object[] { cell });
        
        // remove vertex from hashmaps
        cellToVertexMap.remove(cell);
        vertexToCellMap.remove(e.getVertex());
        
        // remove all edges that connected to the vertex
        ArrayList<E> removedEdges = new ArrayList<E>();
        
        // first, generate a list of all edges that have to be deleted
        // so we don't change the cellToEdgeMap.values by deleting while
        // iterating
        // we have to iterate over this because the graphT has already
        // deleted the vertex and edges so we can't query what the edges were
        for (E edge : cellToEdgeMap.values()) {
            if (!graphT.edgeSet().contains(edge)) {
                removedEdges.add(edge);
            }
        }
        
        // then delete all entries of the previously generated list
        for (E edge : removedEdges) {
            removeEdge(edge);
        }
    }

    @Override
    public final void edgeAdded(final GraphEdgeChangeEvent<V, E> e) {
        addJGraphTEdge(e.getEdge());
    }

    @Override
    public final void edgeRemoved(final GraphEdgeChangeEvent<V, E> e) {
        removeEdge(e.getEdge());
    }
    
    
    
   

    //                     Private Methods
    // ================================================================
    
    /**
     * Removes a jgrapht edge and its visual representation from this graph
     * completely.
     * 
     * @param edge
     *            The edge that will be removed
     */
    private void removeEdge(final E edge) {
        mxCell cell = edgeToCellMap.remove(edge);
        removeCells(new Object[] { cell });
        
        // remove edge from hashmaps
        cellToEdgeMap.remove(cell);
        edgeToCellMap.remove(edge);
    }
    
    
    /**
     * Draws a new vertex into the graph.
     * 
     * @param vertex vertex to be added to the graph
     */
    private void addJGraphTVertex(final V vertex) {

        getModel().beginUpdate();

        try {
            // create a new JGraphX vertex at position 0
            mxCell cell = (mxCell) insertVertex(defaultParent, null, vertex, 
                                                0, 0, 0, 0);
            
            // update cell size so cell isn't "above" graph
            updateCellSize(cell);
            
            // Save reference between vertex and cell
            vertexToCellMap.put(vertex, cell);
            cellToVertexMap.put(cell, vertex);
            
        } finally {
            getModel().endUpdate();
        }
    }

    /**
     * Draws a new egde into the graph.
     * 
     * @param edge edge to be added to the graph. Source and target
     *  vertices are needed.
     */
    private void addJGraphTEdge(final E edge) {

        getModel().beginUpdate();

        try {
            // find vertices of edge
            V sourceVertex = graphT.getEdgeSource(edge);
            V targetVertex = graphT.getEdgeTarget(edge);
            
            // if the one of the vertices is not drawn, don't draw the edge
            if (!(vertexToCellMap.containsKey(sourceVertex) 
               && vertexToCellMap.containsKey(targetVertex))) {
                return;
            }
            
            // get mxCells
            Object sourceCell = vertexToCellMap.get(sourceVertex);
            Object targetCell = vertexToCellMap.get(targetVertex);
            
            // add edge between mxcells
            mxCell cell = (mxCell) insertEdge(defaultParent, null, 
                    null, sourceCell, targetCell);
            
            // update cell size so cell isn't "above" graph
            updateCellSize(cell);
            
            // Save reference between vertex and cell
            edgeToCellMap.put(edge, cell);
            cellToEdgeMap.put(cell, edge);
            
        } finally {
            getModel().endUpdate();
        }
    }
    
    /**
     * Draws a given graph with all its vertices and edges.
     * 
     * @param graph the graph to be added to the existing graph.
     */
    private void insertJGraphT(final Graph<V, E> graph) {
        
        for (V vertex : graph.vertexSet()) {
            addJGraphTVertex(vertex);
        }

        for (E edge : graph.edgeSet()) {
            addJGraphTEdge(edge);
        }

    }
}