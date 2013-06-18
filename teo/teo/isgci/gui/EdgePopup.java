/*
 * Popupmenu for EdgeViews
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/EdgePopup.java,v 2.0 2011/09/25 12:37:13 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jgrapht.graph.DefaultEdge;

import teo.isgci.gc.GraphClass;


public class EdgePopup extends JPopupMenu implements ActionListener {
    ISGCIMainFrame parent;
    JMenuItem deleteItem, infoItem;
    DefaultEdge edge = new DefaultEdge();;

    public EdgePopup(ISGCIMainFrame parent) {
        super();
        this.parent = parent;
        //deleteItem = new JMenuItem("Delete");
        add(infoItem = new JMenuItem("Information"));
        infoItem.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == infoItem) {
            ISGCITabbedPane tabbedPane = parent.getTabbedPane();
            Set<GraphClass> sourceNode = (Set<GraphClass>) tabbedPane
                    .getActiveDrawingLibraryInterface().getGraph()
                    .getEdgeSource(edge);

            Set<GraphClass> targetNode = (Set<GraphClass>) tabbedPane
                    .getActiveDrawingLibraryInterface().getGraph()
                    .getEdgeTarget(edge);
            
            JDialog d = InclusionResultDialog.newInstance(parent,
                sourceNode.iterator().next(),
                targetNode.iterator().next());
            d.setLocation(50, 50);
            d.pack();
            d.setVisible(true);
        } 
    }

    public void setEdge(DefaultEdge edge) {
        this.edge = edge;
        
    }
}

/* EOF */
