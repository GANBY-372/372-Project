package edu.metrostate.ics372.ganby.vehicle

import org.json.simple.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.w3c.dom.Element
import java.time.LocalDateTime
import javax.xml.parsers.DocumentBuilderFactory

class VehicleBuilderTest {

 @Test
 fun `buildVehicleFromJSON returns expected subclass`() {
  val json = JSONObject()
  json["vehicle_id"] = "V001"
  json["vehicle_model"] = "Rav4"
  json["vehicle_manufacturer"] = "Toyota"
  json["price"] = "28000"
  json["dealership_id"] = "D001"
  json["acquisition_date"] = System.currentTimeMillis().toString()
  json["vehicle_type"] = "suv"

  val vehicle = VehicleBuilder.buildVehicleFromJSON(json)
  assertNotNull(vehicle)
  assertTrue(vehicle is SUV)
  assertEquals("Rav4", vehicle!!.getModel())
 }

 @Test
 fun `buildVehicleFromJSON returns null for missing fields`() {
  val badJson = JSONObject()
  badJson["vehicle_id"] = "V001"
  // missing fields

  val vehicle = VehicleBuilder.buildVehicleFromJSON(badJson)
  assertNull(vehicle)
 }

 @Test
 fun `buildVehicleFromJSON returns null for unknown type`() {
  val json = JSONObject()
  json["vehicle_id"] = "V002"
  json["vehicle_model"] = "ModelX"
  json["vehicle_manufacturer"] = "Tesla"
  json["price"] = "80000"
  json["dealership_id"] = "D002"
  json["acquisition_date"] = System.currentTimeMillis().toString()
  json["vehicle_type"] = "spaceship"

  val vehicle = VehicleBuilder.buildVehicleFromJSON(json)
  assertNull(vehicle)
 }

 @Test
 fun `buildVehicleFromXML builds vehicle correctly`() {
  val xmlString = """
            <Vehicle type="Sedan" id="V003">
                <Make>Honda</Make>
                <Model>Civic</Model>
                <Price>23000</Price>
                <is_rented_out>true</is_rented_out>
            </Vehicle>
        """.trimIndent()

  val doc = parseXml(xmlString)
  val element = doc.getElementsByTagName("Vehicle").item(0) as Element
  val vehicle = VehicleBuilder.buildVehicleFromXML(element, "D003", "Some Dealer")

  assertNotNull(vehicle)
  assertTrue(vehicle is Sedan)
  assertEquals("Civic", vehicle!!.getModel())
  assertTrue(vehicle.getIsRentedOut())
 }

 @Test
 fun `buildVehicleFromXML returns null for missing type`() {
  val xml = """
            <Vehicle id="V004">
                <Make>Ford</Make>
                <Model>Focus</Model>
                <Price>18000</Price>
            </Vehicle>
        """.trimIndent()

  val doc = parseXml(xml)
  val element = doc.getElementsByTagName("Vehicle").item(0) as Element
  val vehicle = VehicleBuilder.buildVehicleFromXML(element, "D004", null)

  assertNull(vehicle)
 }

 @Test
 fun `buildVehicle creates correct subclass by type`() {
  val date = LocalDateTime.now()

  val suv = VehicleBuilder.buildVehicle("V1", "X1", "BMW", 40000.0, "D1", date, false, "suv")
  val sedan = VehicleBuilder.buildVehicle("V2", "A4", "Audi", 35000.0, "D2", date, false, "sedan")
  val pickup = VehicleBuilder.buildVehicle("V3", "F150", "Ford", 45000.0, "D3", date, false, "pickup")
  val sportsCar = VehicleBuilder.buildVehicle("V4", "911", "Porsche", 120000.0, "D4", date, false, "sportsCar")
  val unknown = VehicleBuilder.buildVehicle("V5", "Boat", "Yamaha", 80000.0, "D5", date, false, "yacht")

  assertTrue(suv is SUV)
  assertTrue(sedan is Sedan)
  assertTrue(pickup is Pickup)
  assertTrue(sportsCar is SportsCar)
  assertNull(unknown)
 }

 @Test
 fun `buildVehicle throws for null required fields`() {
  val now = LocalDateTime.now()
  assertThrows(IllegalArgumentException::class.java) {
   VehicleBuilder.buildVehicle(null, "model", "maker", 10000.0, "D1", now, false, "suv")
  }
  assertThrows(IllegalArgumentException::class.java) {
   VehicleBuilder.buildVehicle("V1", null, "maker", 10000.0, "D1", now, false, "suv")
  }
  assertThrows(IllegalArgumentException::class.java) {
   VehicleBuilder.buildVehicle("V1", "model", null, 10000.0, "D1", now, false, "suv")
  }
  assertThrows(IllegalArgumentException::class.java) {
   VehicleBuilder.buildVehicle("V1", "model", "maker", 10000.0, null, now, false, "suv")
  }
  assertThrows(IllegalArgumentException::class.java) {
   VehicleBuilder.buildVehicle("V1", "model", "maker", 10000.0, "D1", now, false, null)
  }
 }

 // === Helper to parse XML strings ===
 private fun parseXml(xml: String): org.w3c.dom.Document {
  val factory = DocumentBuilderFactory.newInstance()
  val builder = factory.newDocumentBuilder()
  return builder.parse(xml.byteInputStream())
 }
}
