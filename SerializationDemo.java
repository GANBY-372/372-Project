import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.text.ParseException;

public class SerializationDemo {
    private static String json = "{\n" +
            "   \"car_inventory\":[\n" +
            "      {\n" +
            "         \"dealership_id\":\"12513\",\n" +
            "\t  \"vehicle_type\":\"suv\",\n" +
            "\t  \"vehicle_manufacturer\":\"Ford\",\n" +
            "         \"vehicle_model\":\"Explorer\",\n" +
            "         \"vehicle_id\":\"48934j\",\n" +
            "\t  \"price\": 20123,\n" +
            "\t  \"acquisition_date\": 1515354694451\n" +
            "      },\n" +
            "      {\n" +
            "         \"dealership_id\":\"12513\",\n" +
            "\t  \"vehicle_type\":\"sedan\",\n" +
            "\t  \"vehicle_manufacturer\":\"Tesla\",\n" +
            "         \"vehicle_model\":\"Model 3\",\n" +
            "         \"vehicle_id\":\"83883\",\n" +
            "\t  \"price\": 50444,\n" +
            "\t  \"acquisition_date\": 1515354694451\n" +
            "      },\n" +
            "\t  {\n" +
            "         \"dealership_id\":\"12513\",\n" +
            "\t  \"vehicle_type\":\"pickup\",\n" +
            "\t  \"vehicle_manufacturer\":\"Chevy\",\n" +
            "         \"vehicle_model\":\"Silverado\",\n" +
            "         \"vehicle_id\":\"89343883\",\n" +
            "\t  \"price\": 70444,\n" +
            "\t  \"acquisition_date\": 1515354694451\n" +
            "      },\n" +
            "\t  {\n" +
            "         \"dealership_id\":\"77338\",\n" +
            "\t  \"Vehicle_type\":\"sports car\",\n" +
            "\t  \"vehicle_manufacturer\":\"Toyota\",\n" +
            "         \"vehicle_model\":\"Supra\",\n" +
            "         \"vehicle_id\":\"229393\",\n" +
            "\t  \"price\": 49889,\n" +
            "\t  \"acquisition_date\": 1515354694451\n" +
            "      }\t  \n" +
            "   ]\n" +
            "}";

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try{
            parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            String name = jsonObject.get("vehicle_model");
            System.out.println("Vehicle Model: " + name);

            Result result = new Result(name,score);
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("result.bin"));
            stream.writeObject(result);

            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("result.bin"));

            inStream.readObject();

        } catch (ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}
