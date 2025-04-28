package edu.metrostate.ics372.ganby

import edu.metrostate.ics372.ganby.FXAPP.FXController
import edu.metrostate.ics372.ganby.dataprocessing.PersistenceManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class FXDriver : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("FXAPP-View.fxml"))
        val root = loader.load<Parent>()

        val controller = loader.getController<FXController>()
        PersistenceManager.loadAutosave(controller) // Load saved data

        primaryStage.title = "Vehicle Tracking System"
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    override fun stop() {
        PersistenceManager.saveAutosave() // Save on exit
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch()
        }
    }
}