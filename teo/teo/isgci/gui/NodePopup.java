/*
 * Popupmenu for NodeViews
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/NodePopup.java,v 2.0 2011/09/25 12:37:13 ux Exp $
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

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jgrapht.graph.DefaultEdge;

import teo.isgci.gc.GraphClass;
import teo.isgci.util.Utility;

/**
 * A popup menu which opens when right-clicking a Node in the Graph.
 */
public class NodePopup extends JPopupMenu implements ActionListener {
    ISGCIMainFrame parent;
    JMenuItem deleteItem, infoItem;
    JMenu nameItem;
    Set<GraphClass> node;

    private static String CHANGENAME = "Name: ";

    public NodePopup(ISGCIMainFrame parent) {
        super();
        this.parent = parent;
        //deleteItem = new JMenuItem("Delete");
        add(infoItem = new JMenuItem("Information"));
        add(nameItem = new JMenu("Change name"));
        infoItem.addActionListener(this);
    }


    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == infoItem) {
            JDialog d = new GraphClassInformationDialog(
                    parent, node.iterator().next());
            d.setLocation(50, 50);
            d.pack();
            d.setSize(800, 600);
            d.setVisible(true);
        } else if (event.getActionCommand().startsWith(CHANGENAME)) {
            String fullname = event.getActionCommand().substring(
                    CHANGENAME.length());
            parent.getTabbedPane().getActiveDrawingLibraryInterface().
                getGraphManipulationInterface().renameNode(node, fullname);
            
            parent.getTabbedPane().getSelectedComponent().update(parent.getTabbedPane().getSelectedComponent().getGraphics());
            parent.getTabbedPane().getSelectedComponent().repaint();
        }
    }
    
    public void show(Component orig, int x, int y) {
        int i = 0;

        nameItem.removeAll();
        nameItem.setEnabled(node.size() != 1);
        JMenuItem[] mItem = new JMenuItem[node.size()];
        //FIXME sort and render latex properly
        for (GraphClass gc : node) {
            nameItem.add(mItem[i] = new JMenuItem(
                    Utility.getShortName(gc.toString())));
            mItem[i].setActionCommand(CHANGENAME + gc.toString());
            mItem[i].addActionListener(this);
            i++;
        }
        
        super.show(orig, x, y);
    }

    public void setNode(Set<GraphClass> node) {
        this.node = node;        
    }
}

/* EOF */
