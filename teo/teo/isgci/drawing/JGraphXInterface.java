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

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import javax.swing.JComponent;

/**
 * Dumbed down version of the original, WIP interface
 * TODO: replace this with the final one
 */
public class JGraphXInterface implements DrawingLibraryInterface {

    private mxGraphComponent graphComponent;

    private JGraphXAdapter<String, DefaultEdge> graphAdapter;

    /**
     * The instance of DrawingLibraryInterface.
     */
    public JGraphXInterface(Graph<String, DefaultEdge> g) {

        // Convert to JGraphT-Graph
        graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);

        // Applys a hierarchical layout to the given graph
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // Create the mxGraphComponent used to draw the graph
        graphComponent = new mxGraphComponent(graphAdapter);
    }

    /**
     * Exports the current graph.
     * 
     * @param format
     *            The actual format (.ps, .svg, .graphml)
     * @param path
     *            The path where the graph will be exported to
     */
    @Override
    public final void export(final String format, final String path) {

    }

    /**
     * Returns an Array of all currently implemented export formats.
     * 
     * @return An array of String with the formats
     */
    @Override
    public final String[] getAvailableExportFormats() {
        return new String[] { "ps", "svg", "graphml" };
    }

    @Override
    public final JComponent getPanel() {
        return graphComponent;
    }

    @Override
    public final void setGraph(final ListenableGraph<String, DefaultEdge> g) {
        graphComponent.setGraph(new JGraphXAdapter<String, DefaultEdge>(g));
    }
}