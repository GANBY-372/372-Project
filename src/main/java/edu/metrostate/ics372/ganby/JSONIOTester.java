package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.json.JSONFileExporter;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;

public class JSONIOTester {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {

        // Instantiate JSONFileImporter
        JSONFileImporter jsonFileImporter = new JSONFileImporter();

        System.out.println("Importing vehicles...");
        jsonFileImporter.printDealerKeys();
        jsonFileImporter.processJSON();
        System.out.println(jsonFileImporter.getDealers().toString());

        // Instantiate JSONFileExporter and test the export functionality
        JSONFileExporter jsonFileExporter = new JSONFileExporter();
        jsonFileExporter.exportToFile();
    }
}
