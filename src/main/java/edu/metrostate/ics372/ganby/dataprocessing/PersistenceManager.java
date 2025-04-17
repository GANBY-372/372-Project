package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.FXAPP.FXController;


import java.io.File;

public class PersistenceManager {
    // TODO: We need this class to save all of the dealers and vehicles, and load them back in WITH all state
    //  attributes, UI side or backend?

    private static final String AUTOSAVE_PATH = "src/main/resources/Auto_Save/dealer_catalog_autosave.json";

    /**
     * Loads persisted dealer catalog data (if exists) and updates the FXController.
     *
     * @param controller the FXController to update the UI
     */


    public static void loadAutosave(FXController controller) {
        File autosaveFile = new File(AUTOSAVE_PATH);
        if (autosaveFile.exists()) {
            try {
                JSONFileImporter importer = new JSONFileImporter(AUTOSAVE_PATH);
                importer.processJSON();
                controller.loadData();  // Refresh the UI
                System.out.println("✅ Autosave data loaded from: " + AUTOSAVE_PATH);
            } catch (Exception e) {
                System.err.println("❌ Failed to load autosave data: " + e.getMessage());
            }
        }
    }

    /**
     * Saves the current state of the dealer catalog to a persistent file.
     */


    public static void saveAutosave() {
        try {
            JSONFileExporter.saveStateToFile(AUTOSAVE_PATH);
            System.out.println("💾 Dealer catalog autosaved to: " + AUTOSAVE_PATH);
        } catch (Exception e) {
            System.err.println("❌ Failed to autosave dealer catalog: " + e.getMessage());
        }
    }
}


