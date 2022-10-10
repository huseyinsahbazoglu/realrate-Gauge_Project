import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class BaseSteps {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    JSONObject jo = new JSONObject();

    public static void H2jdbcCreate() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE REGISTRATION " +
                    " (Property VARCHAR(255), " +
                    " Rate VARCHAR(255))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        System.out.println("Create H2 Database Process was succeed.");
    }

    public static void saveRate(WebElement element, String rateType) {
        String Rate = null;
        String rate = element.getText();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            stmt = conn.createStatement();
            String sql0 = "SELECT Rate FROM Registration WHERE Property = '" + rateType + "'";
            ResultSet rs = stmt.executeQuery(sql0);

            while (rs.next()) {

                Rate = rs.getString("Rate");
                //System.out.print(Property +": "+ Rate + " from DB"+ "\n");
            }

            if (Rate == null || Rate == "") {
                String sql1 = "INSERT INTO Registration " + "VALUES ('" + rateType + "', '" + rate + "')";
                stmt.executeUpdate(sql1);
            } else {
                String sql2 = "UPDATE Registration SET Rate = '" + rate + "' WHERE Property = '" + rateType + "'";
                stmt.executeUpdate(sql2);
            }
            System.out.println("Inserted records into the table...");
            System.out.println(rateType + ": " + rate);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRate(String Property) {
        String Rate = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "SELECT Rate FROM Registration WHERE Property='" + Property + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Rate = rs.getString("Rate");
                //System.out.print(Property +": "+ Rate + " from DB"+ "\n");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Rate;
    }

    public static void H2jdbcDelete() {
        Connection conn = null;
        Statement stmt = null;
        try {

            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql = "TRUNCATE TABLE Registration";
            stmt.executeUpdate(sql);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DB Table was deleted!");
    }

    public void stopStmtAndConn() {
        Connection conn = null;
        Statement stmt = null;
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException se2) {
        } // nothing we can do
        try {
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void createHTMLFile(String gold, String dollar, String euro) {
        try {
            File myObj = new File("html/rates.html");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (
                IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Güncel Gram Altın, Dolar, Euro Kurları</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Gram Altın: <span class=\"gold-rate\">" + gold + "</span></p>\n" +
                "    <p>Dolar: <span class=\"dollar-rate\">" + dollar + "</span></p>\n" +
                "    <p>Euro: <span class=\"euro-rate\">" + euro + "</span></p>\n" +
                "\n" +
                "    <script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>\n" +
                "    <script>\n" +
                "        (function() {\n" +
                "            var poll = function() {\n" +
                "                $.ajax({\n" +
                "                url: 'rates.json',\n" +
                "                dataType: 'json',\n" +
                "                type: 'get',\n" +
                "                success: function(data){\n" +
                "                    $('.gold-rate').text(data.Altın);\n" +
                "                    $('.dollar-rate').text(data.Dolar);\n" +
                "                    $('.euro-rate').text(data.Euro);\n" +
                "                },\n" +
                "                error: function () {\n" +
                "                //error\n" +
                "                }\n" +
                "                });\n" +
                "            };\n" +
                "\n" +
                "            poll();\n" +
                "\n" +
                "            setInterval(function() {\n" +
                "                poll();\n" +
                "            }, 20);\n" +
                "        })();\n" +
                "    </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        try {
            FileWriter myWriter = new FileWriter("html/rates.html");
            myWriter.write(html);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void createJSONFile(String gold, String dollar, String euro) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Altın", gold);
        jsonObject.put("Dolar", dollar);
        jsonObject.put("Euro", euro);
        try {
            FileWriter file = new FileWriter("html/rates.json");
            file.write(jsonObject.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: " + jsonObject + "\n");
    }
}