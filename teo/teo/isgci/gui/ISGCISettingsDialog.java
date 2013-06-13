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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A dialog where the user can change various settings.
 * 
 * @param <E>
 * 
 */
public class ISGCISettingsDialog extends JDialog implements ActionListener,
        ListSelectionListener, ChangeListener {

    protected ISGCIMainFrame parent;
    protected JTabbedPane options = new JTabbedPane(JTabbedPane.TOP);
    protected JButton cancelButton, applyButton, okButton, setDefaultButton;
    protected JList colourOptions;
    protected JColorChooser colours;
    protected JComboBox tabOrientation, theme;// nachschlagen
    private String[] tabs = new String[] { "Top", "East", "West", "Bottom" };
    protected JCheckBox toolbar;
    protected JSlider zoomLevel;

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
        JPanel tabCanvas = new JPanel(gridbag);
        JPanel tabUserInterface = new JPanel(gridbag);
        JPanel tabGraphColours = new JPanel(gridbag);
        JPanel userInterface = new JPanel(gridbag);
        JPanel canvas = new JPanel(gridbag);
        JPanel graphColours = new JPanel(gridbag);

        options.add("Canvas", tabCanvas);
        options.addTab("User Interface", tabUserInterface);
        options.addTab("Graph Colours", tabGraphColours);
        this.add(options);

        tabGraphColours.add(colours);

        Border border = new BevelBorder(BevelBorder.RAISED);
        // userInterface.setBackground(Color.WHITE);
        userInterface.setBorder(border);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 20, 5);
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabUserInterface.add(userInterface, c);

        // Set default zoom level option
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        JLabel label = new JLabel("Set default zoom level", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        zoomLevel = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        zoomLevel.setMajorTickSpacing(10);
        zoomLevel.setMinorTickSpacing(1);
        zoomLevel.setPaintTicks(true);
        userInterface.add(zoomLevel, c);
        // ergaenzen zoomlevel, erklaerung*/

        // toolbar show/hide checkbox
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Display Toolbar", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        toolbar = new JCheckBox();
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        userInterface.add(toolbar, c);

        // dropdown menu to choose the placement of the tabs
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Tab Orientation", JLabel.LEFT);
        userInterface.add(label, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabOrientation = new JComboBox(tabs);
        tabOrientation.setSelectedIndex(0);
        tabOrientation.setSize(30, 10);
        userInterface.add(tabOrientation, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set Java Theme", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        theme = new JComboBox(tabs);
        theme.setSelectedIndex(0);
        theme.setSize(30, 10);
        userInterface.add(theme, c);
        
        //Buttons fixen, apply??
        
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 5, 10, 40);
        c.gridwidth = 1;
        setDefaultButton = new JButton("Reset to default");
        tabUserInterface.add(setDefaultButton, c);

        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 10, 5);
        c.gridwidth = GridBagConstraints.RELATIVE;
        okButton = new JButton("Ok");
        tabUserInterface.add(okButton, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        cancelButton = new JButton("Cancel");
        tabUserInterface.add(cancelButton, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        applyButton = new JButton("Apply");
        tabUserInterface.add(applyButton, c);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
                options.setTabPlacement(JTabbedPane.RIGHT);
            } else if (x == 2) {
                options.setTabPlacement(JTabbedPane.LEFT);
            } else if (x == 3) {
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
