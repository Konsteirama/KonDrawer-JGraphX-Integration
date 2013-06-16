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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
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
 */
public class ISGCISettingsDialog extends JDialog {
    
    /** TODO marc JAVADOCS. */
    private JButton applyButton;
    
    /** TODO marc JAVADOCS. */
    private JList colorList;
    
    /** TODO marc JAVADOCS. */
    private JComboBox tabOrientation, themeSelector, namingPreference;
    
    /** Maps a name to a java look and feel theme. */
    private HashMap<String, String> nameToTheme;
    
    /** Maps a name to a specific color, includes all complexities. */
    private HashMap<String, Color> nameToColor;
   
    /** TODO marc JAVADOCS. */
    private JCheckBox toolbarCheck, groupColors;
    
    /** TODO marc JAVADOCS. */
    private JSlider zoomSlider;
    
    /**
     * This field should change every time the class is changed - once it is
     * actually deployed.
     */
    private static final long serialVersionUID = 3L;

    /**
     * Creates a new options dialogue.
     * @param parent
     *          The mainframe from which the dialog is created.
     */
    public ISGCISettingsDialog(ISGCIMainFrame parent) {
        super(parent, "Settings", true);

        // get available themes
        nameToTheme = new HashMap<String, String>();
        
        UIManager.LookAndFeelInfo[] lookAndFeelInfos 
            = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfos) {
            nameToTheme.put(lookAndFeelInfo.getName(),
                              lookAndFeelInfo.getClassName());
        }
        
        // initialize colors
        nameToColor = new HashMap<String, Color>();
        
        nameToColor.put("Background", 
                UserSettings.getCurrentBackgroundColor());
        nameToColor.put("Font", UserSettings.getCurrentFontColor());
        
        for (Complexity c : Complexity.values()) {
            nameToColor.put(c.getComplexityString(), UserSettings.getColor(c));
        }
        
        // Initialize tab 
        JTabbedPane tabContainer 
            = new JTabbedPane(UserSettings.getCurrentTabPlacement());
        
        setLayout(new BorderLayout());
        add(tabContainer, BorderLayout.CENTER);

        // Initialize buttons
        createBottomPanel();

        /*
         * Creates the tabs and the integrated panels.
         */
        tabContainer.addTab("User Interface", createUserInterfaceTab());
        tabContainer.addTab("Graph colors", createColorTab());

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a panel where the user can change settings related to the
     * UserInterface.
     * @return
     *          Panel with various sliders and boxes.
     */
    private JPanel createUserInterfaceTab() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel tabUserInterface = new JPanel(gridbag);
        JPanel userInterface = new JPanel(gridbag);
        
        // CONSTANTS
        final int gap = 5;
        final int bigGap = 10;
        final int width = 30;
        final int height = 10;
        
        /*
         * Adds the user interface panel to the user interface tab.
         */
        Border ui = new BevelBorder(BevelBorder.RAISED);
        userInterface.setBorder(ui);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(gap, gap, bigGap, gap);
        c.weightx = 1.0;
        final double weighty = 0.85;
        c.weighty = weighty;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabUserInterface.add(userInterface, c);

        /*
         * Adds a description and a slider to the user interface panel to set
         * the standard zoom level.
         */
        final int majorSpacing = 10;
        final int minZoom = 0;
        final int maxZoom = 1000;
        
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
        zoomSlider = new JSlider(JSlider.HORIZONTAL, minZoom, maxZoom, 0);
        zoomSlider.setValue(UserSettings.getCurrentZoomLevel());
        zoomSlider.setMajorTickSpacing(majorSpacing);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setPaintTicks(true);
        userInterface.add(zoomSlider, c);

        zoomSlider.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                
                if (!zoomSlider.getValueIsAdjusting()) {
                    enableApplyButton();
                }
            }
        });
        
        /*
         * Adds a description and a checkbox to the user interface panel to 
         * show or hide the toolbar.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Display Toolbar", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        
        toolbarCheck = new JCheckBox();
        toolbarCheck.setSelected(true);
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        userInterface.add(toolbarCheck, c);
        
        toolbarCheck.addActionListener(new ActionListener() {
            
            /*
             * Enables or disables the toolbar 
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbarCheck.setSelected(toolbarCheck.isSelected());
                enableApplyButton();
            }
        });

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the placement of the tab line.
         */
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Tab Orientation", JLabel.LEFT);
        userInterface.add(label, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        /* Needs to stay in that order, because
         *  TOP == 1
         *  LEFT == 2
         *  BOTTOM == 3
         *  RIGHT == 4 
         *  ergo selectedindex == tabplacement - 1
         */
        tabOrientation = new JComboBox(
                new String[] { "Top", "Left", "Bottom", "Right" });
        
        tabOrientation.setSelectedIndex(
                UserSettings.getDefaultTabPlacement() - 1);

        tabOrientation.setSize(width, height);
        userInterface.add(tabOrientation, c);
        
        tabOrientation.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                enableApplyButton();
            }
        });

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the java theme for all windows.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set Java Theme", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        themeSelector = new JComboBox(nameToTheme.keySet().toArray());
        
        // search for current theme name
        String defaultThemeName = "";
        for (String name : nameToTheme.keySet()) {
            if (nameToTheme.get(name).equals(UserSettings.getCurrentTheme())) {
                defaultThemeName = name;
                break;
            }
        }
        themeSelector.setSelectedItem(defaultThemeName);
        
        themeSelector.setSize(width, height);
        userInterface.add(themeSelector, c);
               
        themeSelector.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {                
                    enableApplyButton();                
            }
        });

        /*
         * Adds a description and a combobox to the user interface panel to
         * choose the naming preference.
         */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        label = new JLabel("Set default naming preference", JLabel.LEFT);
        gridbag.setConstraints(label, c);
        userInterface.add(label);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        namingPreference = new JComboBox(Algo.NamePref.values());
        namingPreference.setSelectedItem(0);
        namingPreference.setSize(width, height);
        userInterface.add(namingPreference, c);
        
        namingPreference.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Algo.NamePref namepref 
                    = (Algo.NamePref) namingPreference.getSelectedItem();

                if (namepref != UserSettings.getDefaultNamingPref()) {
                    enableApplyButton();
                }
            }
        });
        
        return userInterface;
    }
    
    
    /**
     * Creates a panel where the user can change colors.
     * @return
     *          Panel with colorchooser and list.
     */
    private JPanel createColorTab() {
        JPanel graphcolors = new JPanel(new GridBagLayout());
        JPanel tabGraphcolors = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        // CONSTANTS
        final int gap = 5;
        final int bigGap = 20;
        final int hugeGap = 40;
        
        /*
         * Adds the graph colors panel to the graph colors tab.
         */
        Border graph = new BevelBorder(BevelBorder.RAISED);
        graphcolors.setBorder(graph);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(gap, gap, bigGap, gap);
        c.weightx = 1.0;
        c.weighty = 0.85;
        c.gridwidth = GridBagConstraints.REMAINDER;
        tabGraphcolors.add(graphcolors, c);

        /*
         * Adds a list, a color chooser and a checkbox to the graph colors
         * panel. The list holds the different problems, for which the user can
         * color the graph. With the color chooser the user can set colors
         * for the options in the list. If the checkbox is activated, the
         * default grouping of problem colors is set.
         */
        
        final JColorChooser colorChooser = new JColorChooser();
        c.gridwidth = GridBagConstraints.RELATIVE;
        colorList = new JList(nameToColor.keySet().toArray());
        colorList.setSelectedIndex(1);
        graphcolors.add(colorList, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(bigGap, gap, bigGap, gap);
        graphcolors.add(colorChooser, c);
        c.anchor = GridBagConstraints.WEST;
        groupColors = new JCheckBox("Group problems");
        groupColors.setSelected(true);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        graphcolors.add(groupColors, c);

        colorChooser.getSelectionModel().addChangeListener(
                new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Color newColor = colorChooser.getColor();

                String name = (String) colorList.getSelectedValue();
                
                if (newColor != nameToColor.get(name)) {
                    nameToColor.put(name, newColor);
                }

            }
        });
        
        colorList.addListSelectionListener(new ListSelectionListener() {

                    /*
                     * Changes the color chooser to the color of the
                     * corresponding selection choosen in the list.
                     */
                    
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        String name 
                            = (String) colorList.getSelectedValue();
                        
                        colorChooser.setColor(nameToColor.get(name));
                    }
                });

        // DEFAULT COLOR BUTTON
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, gap, bigGap, hugeGap);
        c.gridwidth = 1;
        JButton gcSetDefaultButton = new JButton("Default colors");
        tabGraphcolors.add(gcSetDefaultButton, c);
        
        gcSetDefaultButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set default color scheme.
                HashMap<Complexity, Color> colorscheme 
                    = UserSettings.getDefaultColorScheme();
                
                // DO NOT USE colorScheme.keySet() - it's null with enums!
                for (Complexity complexity : Complexity.values()) {
                    nameToColor.put(complexity.getComplexityString(), 
                            colorscheme.get(complexity));
                }
                
                // special colors
                nameToColor.put("Font", UserSettings.getDefaultFontColor());
                nameToColor.put("Background", 
                        UserSettings.getDefaultBackgroundColor());
                
                
                String name = (String) colorList.getSelectedValue();
                colorChooser.setColor(nameToColor.get(name));
                
            }
        });

        // COLORBLIND BUTTON
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, gap, bigGap, gap);
        c.gridwidth = GridBagConstraints.RELATIVE;
        JButton gcColorBlind = new JButton("Set scheme for color blind");
        tabGraphcolors.add(gcColorBlind, c);
        
        gcColorBlind.addActionListener(new ActionListener() {
            
            //Set color blind color scheme.
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                HashMap<Complexity, Color> colorscheme 
                    = UserSettings.getDefaultColorBlindColorScheme();
                
                // DO NOT USE colorScheme.keySet() - it's null with enums!
                for (Complexity complexity : Complexity.values()) {
                    nameToColor.put(complexity.getComplexityString(), 
                            colorscheme.get(complexity));
                }
                
                // special colors
                nameToColor.put("Font", UserSettings.getDefaultFontColor());
                nameToColor.put("Background", 
                        UserSettings.getDefaultBackgroundColor());
                
                String name = (String) colorList.getSelectedValue();
                colorChooser.setColor(nameToColor.get(name));
            }
        });
        
        return tabGraphcolors;
    }
    
    /**
     * Creates the basic layout with tabbedpane and apply etc buttons.
     */
    private void createBottomPanel() {
        // bottomleft
        // to make the button as small as the right buttons
        JPanel bottomLeftPanel = new JPanel(new FlowLayout());

        JButton uiSetDefaultButton = new JButton("Default settings");
        bottomLeftPanel.add(uiSetDefaultButton, BorderLayout.LINE_START);
        
        uiSetDefaultButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                //Set all user interface options to default.
                zoomSlider.setValue(UserSettings.getDefaultZoomLevel());
                tabOrientation.setSelectedIndex(
                        UserSettings.getDefaultTabPlacement() - 1);
                namingPreference.setSelectedItem(
                        UserSettings.getDefaultNamingPref());
                
                // group
                themeSelector.setSelectedIndex(0);
                toolbarCheck.setSelected(true);
            }
        });
        

        // bottomright
        JPanel bottomRightPanel = new JPanel(new FlowLayout());

        // OK Button
        JButton okButton = new JButton("Ok");
        bottomRightPanel.add(okButton);

        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Saves all changes and close the dialogue.
                if (applyButton.isEnabled()) {
                    applySettings();
                }
                closeDialog();
            }
        });
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        bottomRightPanel.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        // Apply button
        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);
        bottomRightPanel.add(applyButton);
        
        applyButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }
        });

        // merge bottom and add to general layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomLeftPanel, BorderLayout.LINE_START);
        bottomPanel.add(bottomRightPanel, BorderLayout.LINE_END);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
    
    /**
     * Enables the apply button.
     */
    private void enableApplyButton() {
        applyButton.setEnabled(true);
    }
    
    /**
     * Writes all settings the UserSettings.
     */
    private void applySettings() {

        // Retrieve java theme
        String javaTheme = "";
        
        Object themeName = themeSelector.getSelectedItem();
        if (nameToTheme.containsKey(themeName)) {
            javaTheme = nameToTheme.get(themeName);
        }
        
        // Saves all changes.
        applyButton.setEnabled(false);
        UserSettings.setNamingPref(
                (Algo.NamePref) namingPreference.getSelectedItem());
        UserSettings.setTabPlacement(
                tabOrientation.getSelectedIndex() + 1);
        UserSettings.setTheme(javaTheme);
        UserSettings.setZoomLevel((int) zoomSlider.getValue());
        UserSettings.setToolbarSetting(toolbarCheck.isSelected());
        
        // set font and background color separately because they're
        // not enums
        UserSettings.setCurrentFontColor(nameToColor.get("Font"));
        UserSettings.setCurrentFontColor(
                nameToColor.get("Background"));
        
        // build a new colorscheme
        HashMap<Complexity, Color> colorScheme 
            = new HashMap<Complexity, Color>();
        
        for (String name : nameToColor.keySet()) {
            Complexity c = Complexity.getComplexity(name);
            
            // Can be null because of font or background
            if (c != null) {
                colorScheme.put(c, nameToColor.get(name));
            }
        }
        
        UserSettings.setColorScheme(colorScheme);
    }
    
    /**
     * Close and dispose the dialogue.
     */
    public void closeDialog() {
        setVisible(false);
        dispose();
    }

}

/* EOF */
