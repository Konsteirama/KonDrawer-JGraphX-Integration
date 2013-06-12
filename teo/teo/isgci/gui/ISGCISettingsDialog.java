/*
 * A dialog where the user can change various settings.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import oracle.jrockit.jfr.Options;

import sun.nio.cs.AbstractCharsetProvider;

/**
 * A dialog where the user can change various settings.
 * 
 * @param <E>
 * 
 */
public class ISGCISettingsDialog extends JDialog implements ActionListener,
        ListSelectionListener, ChangeListener {

    protected ISGCIMainFrame parent;
    JTabbedPane options = new JTabbedPane(JTabbedPane.TOP);
    protected JButton setDefaultButton;
    protected JButton cancelButton;
    protected JButton applyButton;
    protected JButton okButton;
    protected JList<String> colourOptions;
    protected JColorChooser colours;
    protected JComboBox<String> tabOrientation;
    private String[] tabs = new String[] { "Top", "East", "West", "Bottom" };
    protected JComboBox<String> theme; // nachschlagen
    protected JCheckBox toolbar;

    /**
     * This field should change every time the class is changed - once it is
     * actually deployed. (TODO marc)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new dialog. TODO marc
     */
    public ISGCISettingsDialog(ISGCIMainFrame parent) {
        super(parent, "Settings", true);
        this.parent = parent;
        // TODO marc
        // sketch coming soon

        colours = new JColorChooser();
        // Retrieve the current set of panels
        AbstractColorChooserPanel[] oldPanels = colours.getChooserPanels();

        // Remove panels
        for (int i = 0; i < oldPanels.length; i++) {
            String clsName = oldPanels[i].getClass().getName();
            if (clsName
                    .equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
                // Remove swatch chooser
                colours.removeChooserPanel(oldPanels[i]);
            } else if (clsName
                    .equals("javax.swing.colorchooser.DefaultRGBChooserPanel")) {
                // Remove rgb chooser
                colours.removeChooserPanel(oldPanels[i]);
            }
        }

        // JColorChooser.createDialog(null, "Dialog Title", false, colours,
        // null, null).setVisible(true);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        Container canvas = getContentPane();
        Container userInterface = getContentPane();
        Container graphColours = getContentPane();

        canvas.setLayout(gridbag);
        userInterface.setLayout(gridbag);
        graphColours.setLayout(gridbag);

        options.addTab("Canvas", canvas);
        options.addTab("User Interface", userInterface);
        options.addTab("Graph Colours", graphColours);
        this.add(options);

        //Set default zoom level option
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        JLabel label = new JLabel("Set default zoom level", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.weightx = 0.3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        // erg�nzen zoomlevel, erkl�rung

        //toolbar show/hide checkbox
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Display Toolbar", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.weightx = 0.3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(toolbar, c);
        userInterface.add(toolbar);
        
        //tab orientation combobox
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Tab Orientation", JLabel.LEFT);
        c.weightx = 0.3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabOrientation = new JComboBox<String>(tabs);
        tabOrientation.setSelectedIndex(0);

        show();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addListeners();
    }

    protected void addListeners() {
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        applyButton.addActionListener(this);
        setDefaultButton.addActionListener(this);
        tabOrientation.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            closeDialog();
        } else if (source == okButton) {

        } else if (source == applyButton) {

        } else if (source == tabOrientation) {
            int x = tabOrientation.getSelectedIndex();
            if (x == 0) {
                options.setTabPlacement(JTabbedPane.TOP);
            } else if (x == 1) {
                options.setTabPlacement(JTabbedPane.EAST);
            } else if (x == 2) {
                options.setTabPlacement(JTabbedPane.WEST);
            } else {
                options.setTabPlacement(JTabbedPane.BOTTOM);
            }
        }
    }

    public void closeDialog() {
        setVisible(false);
        dispose();
    }

    public void valueChanged(ListSelectionEvent event) {

    }

    public void stateChanged(ChangeEvent event) {

    }

}
/* EOF */
