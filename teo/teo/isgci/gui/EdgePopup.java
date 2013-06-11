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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jgrapht.graph.DefaultEdge;

import teo.isgci.gc.GraphClass;


public class EdgePopup extends JPopupMenu implements ActionListener {
    ISGCIMainFrame parent;
    JMenuItem deleteItem, infoItem;
    EdgeView<Set<GraphClass>,DefaultEdge> view;

    public EdgePopup(ISGCIMainFrame parent) {
        super();
        this.parent = parent;
        //deleteItem = new JMenuItem("Delete");
        add(infoItem = new JMenuItem("Information"));
        infoItem.addActionListener(this);
    }

    public void setEdge(EdgeView n) {
        view = n;
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == infoItem) {
            Component tab = parent.getTabbedPane().getSelectedComponent();
            tab.getComponentAt(tab.getMousePosition());
            System.out.println(tab.getComponentAt(tab.getMousePosition()).toString());
//            JDialog d = InclusionResultDialog.newInstance(parent, DataSet.getClass(tab.getComponentAt(tab.getMousePosition()).get), v2)
            // TODO jannis
//            JDialog d = InclusionResultDialog.newInstance(parent,
//                DataSet.getClass(
//                    parent.getActiveCanvas().getView(view.getFrom()).getFullName()),
//                DataSet.getClass(
//                    parent.getActiveCanvas().getView(view.getTo()).getFullName()));
//            d.setLocation(50, 50);
//            d.pack();
//            d.setVisible(true);
        } 
    }
}

/* EOF */
