/*
 * Select the naming preference for graphclasses.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/SearchDialog.java,v 2.0 2011/09/25 12:37:13 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import teo.isgci.db.Algo;
import teo.isgci.gc.GraphClass;
import teo.isgci.util.LessLatex;
import teo.isgci.util.Updatable;
import teo.isgci.util.UserSettings;

public class SearchDialog extends JDialog 
        implements ActionListener, Updatable {
    protected ISGCIMainFrame parent;
    protected ButtonGroup group;
    protected JCheckBox basicBox, derivedBox, forbiddenBox;
    protected JButton searchButton, cancelButton;
    protected NodeList classesList;

    public SearchDialog(ISGCIMainFrame parent) {
        super(parent, "Search for a graphclass", true);
        this.parent = parent;
        group = new ButtonGroup();

        Algo.NamePref mode = parent.getTabbedPane().getNamingPref(
                parent.getTabbedPane().getSelectedComponent());

        Container content = getContentPane();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gridbag);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 5, 5, 5);
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        classesList = new NodeList();
        classesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroller = new JScrollPane(classesList);
        gridbag.setConstraints(scroller, c);
        content.add(scroller);

        c.insets = new Insets(0, 5, 0, 0);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;

        JPanel p = new JPanel();
        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel");
        p.add(searchButton);
        p.add(cancelButton);
        c.insets = new Insets(5, 0, 5, 0);
        gridbag.setConstraints(p, c);
        content.add(p);

        searchButton.addActionListener(this);
        cancelButton.addActionListener(this);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Set setNames = parent.getTabbedPane()
                .getActiveDrawingLibraryInterface().getGraph().vertexSet();
        List<GraphClass> listNames = new ArrayList<GraphClass>();
        for (Object object : setNames) {
            for (Object name : (Set) object) {
                listNames.add((GraphClass) name);
            }
        }
        if (!listNames.isEmpty()) {
            Collections.sort(listNames, new LessLatex());
            classesList.setListData(listNames);
        }

        pack();
        setSize(300, 350);

        UserSettings.subscribeToOptionChanges(this);
    }

    protected void closeDialog() {
        UserSettings.unsubscribe(this);
        setVisible(false);
        dispose();
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            closeDialog();
        } else if (source == searchButton) {
            Set<GraphClass> node = null;
            Set setNames = parent.getTabbedPane()
                    .getActiveDrawingLibraryInterface().getGraph().vertexSet();
            List<GraphClass> listNames = new ArrayList<GraphClass>();
            for (Object object : setNames) {
                Set names = (Set) object;
                if (names.contains(classesList.getSelectedValue()))
                    node = (Set<GraphClass>) object;
            }
            parent.getTabbedPane().getActiveDrawingLibraryInterface()
                    .getGraphManipulationInterface().centerNode(node);
            closeDialog();
        }
    }

    @Override
    public void updateOptions() {
        try {
            UIManager.setLookAndFeel(UserSettings.getCurrentTheme());
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        SwingUtilities.updateComponentTreeUI(this);
        pack();
        
    }
}

/* EOF */
