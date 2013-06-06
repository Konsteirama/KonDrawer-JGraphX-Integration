/*
 * This interface is used by classes that are affected by values in the options
 * menu. When an option changes, the optionmenu will call update on all
 * registered Updatabales.
 *
 * $Header$
 *
 * This file is part of the Information System on Graph Classes and their
 * Inclusions (ISGCI) at http://www.graphclasses.org.
 * Email: isgci@graphclasses.org
 */


package teo.isgci.util;

/**
 * This interface is used by classes that are affected by values in the options
 * menu. When an option changes, the optionmenu will call update on all
 * registered Updatabales.
 */
public interface Updatable {

    /**
     * Updates the implementing class to adapt to the new options.
     * For example, the corresponding implementation of DrawingLibraryInterface
     * should implement this interface to react to usercolors.
     */
    void updateOptions();
}

/* EOF */
