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
import java.util.HashMap;

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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import teo.isgci.db.Algo;
import teo.isgci.problem.Complexity;
import teo.isgci.util.UserSettings;

/**
 * A dialog where the user can change various settings.
 * 
 * @param <E>
 * 
 */
public class ISGCISettingsDialog extends JDialog implements ActionListener,
        ListSelectionListener, ChangeListener {

    protected JPanel graphColours;
    protected ISGCIMainFrame parent;
    protected JTabbedPane options = new JTabbedPane(/*
                                                     * UserSettings.getTabPlacement
                                                     * ()
                                                     */JTabbedPane.TOP);
    protected JButton cancelButton, applyButton, okButton, uiSetDefaultButton;
    protected JButton gcSetDefaultButton, gcColorBlind;
    protected JList colourOptions;
    protected JColorChooser colours;
    protected JComboBox tabOrientation, theme, namingPreference;// nachschlagen
    private String[] tabs = new String[] { "Top", "East", "West", "Bottom" };
    private String[] themes = new String[] { "Metal", "Nimbus",
            "Platform standard" };
    private String[] problemes = new String[] { "font colour",
            "backgroundcolour", "unkown complexity",
            "GraphIsomorphism-complete", "NP-complete", "lineartime solvable",
            "P", "open classes", "CONPC", "NPH", "keine" };
    private String[] naming = new String[] { "Basic", "Forbidden", "Derived" };
    protected JCheckBox toolbar, groupColours;
    protected JSlider zoomLevel;
    private Color unknown = UserSettings.getColor(Complexity.UNKNOWN),
            gic = UserSettings.getColor(Complexity.GIC), npc = UserSettings
                    .getColor(Complexity.NPC), linear = UserSettings
                    .getColor(Complexity.LINEAR), p = UserSettings
                    .getColor(Complexity.P), open = UserSettings
                    .getColor(Complexity.OPEN), conpc = UserSettings
                    .getColor(Complexity.CONPC), nph = UserSettings
                    .getColor(Complexity.NPH), empty = UserSettings
                    .getColor(null), text, background;
    private HashMap<Complexity, Color> colorscheme = new HashMap<Complexity, Color>();
    private Boolean group = true;
    private Boolean setToolbar = false, setTheme = false, setZoom = false,
            setTabOrientation = false, setNamingPreferences = false;

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

        /*
         * userinterface tab
         */
        Border ui = new BevelBorder(BevelBorder.RAISED);
        // userInterface.setBackground(Color.WHITE);
        userInterface.setBorder(ui);
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
        toolbar.setSelected(true);
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
        theme = new JComboBox(themes);
        theme.setSelectedIndex(0);
        theme.setSize(30, 10);
        userInterface.add(theme, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set naming preference", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        namingPreference = new JComboBox(naming);
        namingPreference.setSelectedIndex(0);
        namingPreference.setSize(30, 10);
        userInterface.add(namingPreference, c);

        /*
         * Buttons fixen, apply??
         */
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 5, 10, 40);
        c.gridwidth = 1;
        uiSetDefaultButton = new JButton("Default settings");
        tabUserInterface.add(uiSetDefaultButton, c);

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
        applyButton.setEnabled(false);
        tabUserInterface.add(applyButton, c);

        /*
         * graph colours tab
         */
        Border graph = new BevelBorder(BevelBorder.RAISED);
        // userInterface.setBackground(Color.WHITE);
        graphColours.setBorder(graph);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 20, 5);
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabGraphColours.add(graphColours, c);

        c.gridwidth = GridBagConstraints.RELATIVE;
        colourOptions = new JList(problemes);
        colourOptions.setSelectedIndex(1);
        graphColours.add(colourOptions, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 5, 10, 5);
        graphColours.add(colours, c);
        c.anchor = GridBagConstraints.WEST;
        groupColours = new JCheckBox("Group problems");
        groupColours.setSelected(true);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        graphColours.add(groupColours, c);

        /*
         * Buttons fixen, apply??
         */
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 5, 10, 40);
        c.gridwidth = 1;
        gcSetDefaultButton = new JButton("Default Colours");
        tabGraphColours.add(gcSetDefaultButton, c);

        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 10, 5);
        c.gridwidth = GridBagConstraints.RELATIVE;
        okButton = new JButton("Ok");
        tabGraphColours.add(okButton, c);

        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 10, 5);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gcColorBlind = new JButton("Set scheme for color blind");
        tabGraphColours.add(gcColorBlind, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        cancelButton = new JButton("Cancel");
        tabGraphColours.add(cancelButton, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);
        tabGraphColours.add(applyButton, c);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        applyButton.addActionListener(this);
        uiSetDefaultButton.addActionListener(this);

        gcSetDefaultButton.addActionListener(this);
        gcColorBlind.addActionListener(this);
        groupColours.addActionListener(this);

        tabOrientation.addActionListener(this);
        theme.addActionListener(this);
        toolbar.addActionListener(this);
        colourOptions.addListSelectionListener(this);
        colours.getSelectionModel().addChangeListener(this);
        namingPreference.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            closeDialog();
        } else if (source == okButton) {
            if (applyButton.isEnabled()) {
                applyButton.doClick();
            }
            closeDialog();
        } else if (source == uiSetDefaultButton) {

        } else if (source == gcColorBlind) {
            colorscheme = UserSettings.getDefaultColorBlindColorScheme();
            for (Complexity complexity : colorscheme.keySet()) {
                if (complexity == Complexity.UNKNOWN) {
                    unknown = colorscheme.get(complexity);
                } else if (complexity == Complexity.CONPC) {
                    conpc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPC) {
                    npc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPH) {
                    nph = colorscheme.get(complexity);
                } else if (complexity == null) {
                    empty = colorscheme.get(complexity);
                } else if (complexity == Complexity.P) {
                    p = colorscheme.get(complexity);
                } else if (complexity == Complexity.GIC) {
                    gic = colorscheme.get(complexity);
                } else if (complexity == Complexity.LINEAR) {
                    linear = colorscheme.get(complexity);
                } else if (complexity == Complexity.OPEN) {
                    open = colorscheme.get(complexity);
                }
            }
            int i = colourOptions.getSelectedIndex();
            colourOptions.clearSelection();
            colourOptions.setSelectedIndex(i);
        } else if (source == applyButton) {
            applyButton.setEnabled(false);
            if (setNamingPreferences) {

            }
            if (setTabOrientation) {

            }
            if (setTheme) {

            }
            if (setZoom) {

            }
            if (setToolbar) {

            }
            UserSettings.setColorScheme(colorscheme);
            colorscheme.clear();
        } else if (source == gcSetDefaultButton) {
            colorscheme = UserSettings.getDefaultColorScheme();
            for (Complexity complexity : colorscheme.keySet()) {
                if (complexity == Complexity.UNKNOWN) {
                    unknown = colorscheme.get(complexity);
                } else if (complexity == Complexity.CONPC) {
                    conpc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPC) {
                    npc = colorscheme.get(complexity);
                } else if (complexity == Complexity.NPH) {
                    nph = colorscheme.get(complexity);
                } else if (complexity == null) {
                    empty = colorscheme.get(complexity);
                } else if (complexity == Complexity.P) {
                    p = colorscheme.get(complexity);
                } else if (complexity == Complexity.GIC) {
                    gic = colorscheme.get(complexity);
                } else if (complexity == Complexity.LINEAR) {
                    linear = colorscheme.get(complexity);
                } else if (complexity == Complexity.OPEN) {
                    open = colorscheme.get(complexity);
                }
            }
            groupColours.setSelected(true);
            int i = colourOptions.getSelectedIndex();
            colourOptions.clearSelection();
            colourOptions.setSelectedIndex(i);
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
            setTabOrientation = true;
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        } else if (source == theme) {
            int i = theme.getSelectedIndex();
            if (i == 0) {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(this);
                this.pack();
            } else if (i == 1) {
                for (LookAndFeelInfo info : UIManager
                        .getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        try {
                            UIManager.setLookAndFeel(info.getClassName());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (UnsupportedLookAndFeelException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                SwingUtilities.updateComponentTreeUI(this);
                this.pack();
            } else if (i == 2) {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(this);
                this.pack();
            }
            setTheme = true;
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        } else if (source == toolbar) {
            toolbar.setSelected(toolbar.isSelected());
            setToolbar = (!setToolbar);
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        } else if (source == groupColours) {
            group = groupColours.isSelected();
        } else if (source == namingPreference) {
            int i = namingPreference.getSelectedIndex();
            if (i == 0) {

            } else if (i == 1) {

            } else if (i == 2) {

            }
            setNamingPreferences = true;
            if (!applyButton.isEnabled()) {
                applyButton.setEnabled(true);
            }
        }
    }

    public void closeDialog() {
        setVisible(false);
        dispose();
    }

    public void valueChanged(ListSelectionEvent event) {
        int i = colourOptions.getSelectedIndex();
        if (i == 0) {
            colours.setColor(text);
        } else if (i == 1) {
            colours.setColor(background);
        } else if (i == 2) {
            colours.setColor(unknown);
        } else if (i == 3) {
            colours.setColor(gic);
        } else if (i == 4) {
            colours.setColor(npc);
        } else if (i == 5) {
            colours.setColor(linear);
        } else if (i == 6) {
            colours.setColor(p);
        } else if (i == 7) {
            colours.setColor(open);
        } else if (i == 8) {
            colours.setColor(conpc);
        } else if (i == 9) {
            colours.setColor(nph);
        } else if (i == 10) {
            colours.setColor(empty);
        }
    }

    public void stateChanged(ChangeEvent event) {
        Color newColor = colours.getColor();
        int s = colourOptions.getSelectedIndex();
        if (s == 0) {
            if (newColor != text) {
                text = newColor;
                applyButton.setEnabled(true);
            }
        } else if (s == 1) {
            if (newColor != background) {
                background = newColor;
                applyButton.setEnabled(true);
            }
        } else if (s == 2) {
            if (newColor != unknown) {
                unknown = newColor;
                colorscheme.remove(unknown);
                colorscheme.put(Complexity.UNKNOWN, unknown);
                applyButton.setEnabled(true);
            }
            if (group) {
                empty = newColor;
                open = newColor;
            }
        } else if (s == 3) {
            if (newColor != gic) {
                gic = newColor;
                colorscheme.remove(gic);
                colorscheme.put(Complexity.GIC, gic);
                applyButton.setEnabled(true);
            }
        } else if (s == 4) {
            if (newColor != npc) {
                npc = newColor;
                colorscheme.remove(npc);
                colorscheme.put(Complexity.NPC, npc);
                applyButton.setEnabled(true);
            }
            if (group) {
                nph = newColor;
                conpc = newColor;
            }
        } else if (s == 5) {
            if (newColor != linear) {
                linear = newColor;
                colorscheme.remove(linear);
                colorscheme.put(Complexity.LINEAR, linear);
                applyButton.setEnabled(true);
            }
        } else if (s == 6) {
            if (newColor != p) {
                p = newColor;
                colorscheme.remove(p);
                colorscheme.put(Complexity.P, p);
                applyButton.setEnabled(true);
            }
        } else if (s == 7) {
            if (newColor != open) {
                open = newColor;
                colorscheme.remove(open);
                colorscheme.put(Complexity.OPEN, open);
                applyButton.setEnabled(true);
            }
            if (group) {
                unknown = newColor;
                empty = newColor;
            }
        } else if (s == 8) {
            if (newColor != conpc) {
                conpc = newColor;
                colorscheme.remove(conpc);
                colorscheme.put(Complexity.CONPC, conpc);
                applyButton.setEnabled(true);
            }
            if (group) {
                nph = newColor;
                npc = newColor;
            }
        } else if (s == 9) {
            if (newColor != nph) {
                nph = newColor;
                colorscheme.remove(nph);
                colorscheme.put(Complexity.NPH, nph);
                applyButton.setEnabled(true);
            }
            if (group) {
                npc = newColor;
                conpc = newColor;
            }
        } else if (s == 10) {
            if (newColor != empty) {
                empty = newColor;
                colorscheme.remove(empty);
                colorscheme.put(null, empty);
                applyButton.setEnabled(true);
            }
            if (group) {
                unknown = newColor;
                open = newColor;
            }
        }
    }

}
/* EOF */
