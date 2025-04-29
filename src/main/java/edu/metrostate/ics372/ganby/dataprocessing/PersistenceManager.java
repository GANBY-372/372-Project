package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.FXAPP.FXController;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;

import java.io.File;
import java.util.ArrayList;

public class PersistenceManager {

    private static final String AUTOSAVE_PATH = "src/main/resources/Auto_Save/dealer_catalog_autosave.xml";

    /**
     * Loads persisted dealer catalog data (XML) and updates the FXController.
     */
    public static void loadAutosave(FXController controller) {
        File autosaveFile = new File(AUTOSAVE_PATH);
        if (autosaveFile.exists()) {
            try {
                XMLFileImporter importer = new XMLFileImporter(AUTOSAVE_PATH);
                importer.processXML();
                controller.loadData();  // Refresh the UI
                System.out.println("✅ Autosave data loaded from: " + AUTOSAVE_PATH);
            } catch (Exception e) {
                System.err.println("❌ Failed to load autosave data: " + e.getMessage());
            }
        }
    }

    /**
     * Saves the current state of the dealer catalog to XML for autosave.
     */
    public static void saveAutosave() {
        try {
            File autosaveFile = new File(AUTOSAVE_PATH);
            XMLFileExporter.exportToFile(autosaveFile, new ArrayList<>(DealerCatalog.getInstance().getDealerCatalog().values()), false);
            System.out.println("💾 Dealer catalog autosaved to: " + AUTOSAVE_PATH);
        } catch (Exception e) {
            System.err.println("❌ Failed to autosave dealer catalog: " + e.getMessage());
        }
    }
}
