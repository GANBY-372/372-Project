package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.FXAPP.FXController;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;

import java.io.File;
import java.util.ArrayList;

/**
 * Utility class responsible for persisting and restoring the dealer catalog data.
 * Used to autosave application state to XML and reload it upon startup.
 */
public class PersistenceManager {

    private static final String AUTOSAVE_PATH = "src/main/resources/Auto_Save/dealer_catalog_autosave.xml";

    /**
     * Loads autosaved dealer and vehicle data from an XML file.
     * Populates the DealerCatalog and updates the UI via the FXController.
     *
     * @param controller the controller instance whose UI should be refreshed after loading
     */
    public static void loadAutosave(FXController controller) {
        File autosaveFile = new File(AUTOSAVE_PATH);
        if (autosaveFile.exists()) {
            try {
                XMLFileImporter importer = new XMLFileImporter(AUTOSAVE_PATH);
                importer.processXML(true); // true = ignore acquisition status on load
                controller.loadData(); // Update UI bindings
                System.out.println("✅ Autosave data loaded from: " + AUTOSAVE_PATH);
            } catch (Exception e) {
                System.err.println("❌ Failed to load autosave data: " + e.getMessage());
            }
        }
    }

    /**
     * Saves the current dealer catalog (including all vehicles) to the autosave XML path.
     * Can be called on exit or periodically to persist user changes.
     */
    public static void saveAutosave() {
        try {
            File autosaveFile = new File(AUTOSAVE_PATH);
            XMLFileExporter.exportToFile(
                    autosaveFile,
                    new ArrayList<>(DealerCatalog.getInstance().getDealerCatalog().values()),
                    false // false = include acquisition status in output
            );
            System.out.println("💾 Dealer catalog autosaved to: " + AUTOSAVE_PATH);
        } catch (Exception e) {
            System.err.println("❌ Failed to autosave dealer catalog: " + e.getMessage());
        }
    }
}
