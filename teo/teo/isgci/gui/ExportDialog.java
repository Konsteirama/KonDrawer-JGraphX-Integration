/*
 * Export dialog.
 *
 * $Header: /home/ux/CVSROOT/teo/teo/isgci/gui/ExportDialog.java,v 2.0 2011/09/25 12:37:13 ux Exp $
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */

package teo.isgci.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import teo.isgci.drawing.DrawingLibraryInterface;

public class ExportDialog extends JDialog implements ActionListener {

    /** The card titles (and ids) */
    protected static final String CARD_FORMAT ="Please choose the file format";
    protected static final String CARD_FILE = "Destination file";
    protected String current;

    /* Global items */
    protected ISGCIMainFrame parent;
    protected JLabel title;
    protected JPanel cardPanel;
    protected CardLayout cardLayout;
    protected JButton backButton, nextButton, cancelButton;

    /* Format items */
    protected ButtonGroup formats;

    /* Save location items */
    protected JFileChooser file;

    public ExportDialog(ISGCIMainFrame parent) {
        super(parent, "Export drawing", true);
        this.parent = parent;

        Container content = getContentPane();
        JPanel buttonPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10))); 

        cardLayout = new CardLayout(); 
        cardPanel.setLayout(cardLayout);

        backButton = new JButton("< Back");
        nextButton = new JButton("Next >");
        cancelButton = new JButton("Cancel");

        backButton.addActionListener(this);
        nextButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10))); 
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);

        title = new JLabel("");
        Font f = title.getFont();
        title.setFont(f.deriveFont((float) (f.getSize() * 1.2)));
        title.setOpaque(true);
        title.setBackground(Color.darkGray);
        title.setForeground(Color.white);
        title.setBorder(new EmptyBorder(new Insets(10,10,10,10)));
        cardPanel.setBorder(new EmptyBorder(new Insets(5,40,5,40)));
        content.add(title, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(cardFormat(), CARD_FORMAT);
        cardPanel.add(cardFile(), CARD_FILE);

        showCard(CARD_FORMAT);
    }


    /**
     * Show the given card and adjust button settings etc. for it.
     */
    protected void showCard(String card) {
        title.setText(card);
        current = card;
        cardLayout.show(cardPanel,card);
        backButton.setEnabled(card != CARD_FORMAT);
        //nextButton.setText(card == CARD_FILE ? "Export" : "Next >");

        // Use the JFileChooser buttons instead, to prevent errors when the
        // user clicks Next, but didn't press Return after typing a file name.
        nextButton.setVisible(card != CARD_FILE);
        cancelButton.setVisible(card != CARD_FILE);
    }

    /**
     * Return the card where the user can select the file format.
     */
    private Component cardFormat() {        
        Box box = new Box(BoxLayout.Y_AXIS);

        formats = new ButtonGroup();
        boolean first = true;
        
        DrawingLibraryInterface drawInterface 
                = parent.getTabbedPane().getActiveDrawingLibraryInterface();
        for (String format : drawInterface.getAvailableExportFormats()) {
            JRadioButton radioB = new JRadioButton(format);
            radioB.setAlignmentX(Component.LEFT_ALIGNMENT);
            formats.add(radioB);
            box.add(radioB);
            radioB.setSelected(first);
            first = false;
            
            // adding description
            if (format.equals("ps")) {
                box.add(explText(
                    "A Postscript file can be included immediately in e.g. LaTeX\n"+
                    "documents, but it cannot easily be edited."));
            } else if (format.equals("svg")) {
                box.add(explText(
                    "An SVG file is suitable for editing the diagram, e.g. with\n"+
                    "inkscape (http://www.inkscape.org), but cannot be included\n"+
                    "directly in LaTeX."));                
            } else if (format.equals("graphml")) {
                box.add(explText(
                    "A graphml file contains the structure of the graph and is\n"+
                    "suitable for processing by many graph tools, but does not\n"+
                    "contain layout information and cannot be included directly\n"+
                    "in LaTeX. Editing and laying out can be done with e.g. yEd.\n"+
                    "(http://www.yworks.com)"));                
            } else if (format.equals("jpg")) {
                box.add(explText(
                    "A JPG file is one of the current standard file formats for\n" + 
                    "images. It can be viewed and processed on nearly any device\n" + 
                    "or any image-processing application. It is more comprimated\n"+
                    "than the PNG format."));                
            } else if (format.equals("png")) {
                box.add(explText(
                    "A PNG file is one of the current standard file formats for\n" + 
                    "images. It can be viewed and processed on nearly any device\n" + 
                    "or any image-processing application. It can be transparent.\n"));
            }
        }

        JPanel p = new JPanel();
        p.add(box, BorderLayout.CENTER);
        return p;
    }

    /**
     * Return the card where the user can select the destination file.
     */
    private Component cardFile() {
        file = new JFileChooser();
        file.setApproveButtonText("Export");
        file.addActionListener(this);
        //file.setControlButtonsAreShown(false); doesn't work - see showCard()
        return file;
    }

    /**
     * Returns a component with explanation of e.g. a radiobutton.
     */
    private Component explText(String text) {
        JTextArea t = new JTextArea(text);
        //t.setLineWrap(true);
        //t.setWrapStyleWord(true);
        t.setEditable(false);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        t.setOpaque(false);
        t.setBorder(new EmptyBorder(new Insets(0,20,0,0)));
        return t;
    }

    public void closeDialog() {
        setVisible(false);
        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == cancelButton)
            closeDialog();
        else if (source == nextButton) {
            if (current == CARD_FORMAT)
                showCard(CARD_FILE);
        } else if (source == backButton) {
                showCard(CARD_FORMAT);
        } else if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
            if (export())
                closeDialog();
        } else if (e.getActionCommand()== JFileChooser.CANCEL_SELECTION)
            closeDialog();
    }

    /**
     * Export using the entered settings. Return true iff no error occured.
     */
    protected boolean export() {
        
        boolean res = true;
        FileOutputStream f;
        try {
            f = new FileOutputStream(file.getSelectedFile());
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.error(parent, "Cannot open file for writing:\n" 
                + file.getSelectedFile().getPath());
            return false;
        }

        try {
            DrawingLibraryInterface drawInterface 
                = parent.getTabbedPane().getActiveDrawingLibraryInterface();
            String chosenFormat = "";
            Enumeration<AbstractButton> buttons = formats.getElements();
            for (int i = 0; i < formats.getButtonCount(); i++) {
                if (buttons.nextElement().getModel()
                        .equals(formats.getSelection())) {
                    chosenFormat 
                        = drawInterface.getAvailableExportFormats()[i];
                }
            }
            String test = file.getSelectedFile().getName();
            test = file.getSelectedFile().getPath();
            if (!file.getSelectedFile().getName().endsWith("." 
                    + chosenFormat)) {
                drawInterface.export(chosenFormat
                        , file.getSelectedFile().getPath()  
                        + "." + chosenFormat);
            } else {
                drawInterface.export(chosenFormat
                        , file.getSelectedFile().getPath());
            }
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
            MessageDialog.error(parent, "Error while exporting:\n"
                    + e.toString());
        }
        return res;
    }
}

/* EOF */
