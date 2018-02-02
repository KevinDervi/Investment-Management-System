package main.java.data.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DBValuesConvertToJava {
    private DBValuesConvertToJava() {
    }

    /**
     * input a result set and have it coverted into a hashmap of column name and value pairs
     */
    @SuppressWarnings("Duplicates")
    protected static Map<String, Object> convertToSingleHashMap(ResultSet resultSet) throws SQLException {
        Map<String, Object> data = null;

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        //TODO if number of rows greater than 1 then make a list of hash maps otherwise return a single (or make another method for converting lists)
        //TODO check that there is only a single value in the result set

        int noOfColumns = resultSetMetaData.getColumnCount();

        System.out.println();
        if (resultSet.next()) {
            //System.out.println("contains at least 1 value");
            data = new HashMap<>();

            for (int i = 1; i <= noOfColumns; i++) {

                String columnName = resultSetMetaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                System.out.println("column name: " + columnName);
                System.out.println("value: " + value);
                if (value != null) {
                    System.out.println("value type: " + value.getClass().getTypeName());
                }
                System.out.println();
                data.put(columnName, value);


            }
        }


        return data;
    }

    @SuppressWarnings("Duplicates")
    protected static ArrayList<Map<String, Object>> convertToHashMapArrayList(ResultSet resultSet) throws SQLException {
        ArrayList<Map<String, Object>> data = new ArrayList<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int noOfColumns = resultSetMetaData.getColumnCount();

        while(resultSet.next()){
            Map<String, Object> tempHashMap = new HashMap<>();
            for (int i = 1; i <= noOfColumns; i++) {

                String columnName = resultSetMetaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                System.out.println("column name: " + columnName);
                System.out.println("value: " + value);
                if (value != null) {
                    System.out.println("value type: " + value.getClass().getTypeName());
                }
                System.out.println();
                tempHashMap.put(columnName, value);


            }
            data.add(tempHashMap);

        }

        return data;
    }
}
