/**
 * Main.java
 * @author GANBY
 */
package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;


public class Main {
    public static void main (String[] args) throws FileNotFoundException, IllegalAccessException {

        // FILE EXPLORER TO SELECT JSON FILE
        JSONFileImporter jsonFileImporter = new JSONFileImporter();

        // Process the JSON file.
        jsonFileImporter.processJSON();

        // print list of the dealer IDs
        System.out.println(DealerCatalog.getInstance().getDealers().size() + " dealers imported into catalog");

        // print out the JSON file data
    }
}