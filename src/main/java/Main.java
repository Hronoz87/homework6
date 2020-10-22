import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.IIOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJSON(list);

        writeString(json);

        List<Employee> list1 = parseXML("data.xml");

        String json1 = listToJSON(list1);

        writeString(json1);
    }

    private static List<Employee> parseXML(String s) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> employee1 = new ArrayList<>();


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(s));

            Node root = doc.getDocumentElement();

            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;
                    String id1 = employee.getElementsByTagName("id").item(0).getTextContent();
                    long id = Long.parseLong(id1);
                    String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employee.getElementsByTagName("country").item(0).getTextContent();
                    String age1 = employee.getElementsByTagName("age").item(0).getTextContent();
                    int age = Integer.parseInt(age1);
                    employee1.add(new Employee(id, firstName, lastName, country, age));
                }
            }


            return employee1;


        }


        public static List<Employee> parseCSV (String[]name, String fileName){

            List<Employee> resultList = new ArrayList<>();

            try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
                ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
                strategy.setType(Employee.class);
                strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");

                CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                        .withMappingStrategy(strategy)
                        .build();

                resultList = csv.parse();
                resultList.forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultList;

        }

        public static <T > String listToJSON(List < Employee > name) {
            Type listType = new TypeToken<List<T>>() {
            }.getType();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(name, listType);

            return json;
        }

        public static void writeString (String json){
            try (FileWriter writer = new FileWriter("data.json", false)) {
                writer.write(json);
                writer.append('\n');
                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
