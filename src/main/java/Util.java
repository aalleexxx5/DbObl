import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Alex on 04/04/2017.
 */
public class Util {
    private static final String TABLE_HORIZONTAL_SEPERATOR = "=================================================================================================================================";

    public static void printTable(ResultSet rs) throws SQLException {
        try{
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            while (rs.next()){
                ArrayList<String> columns = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    columns.add(rs.getString(i));
                }
                rows.add(columns);
            }

            for (int i = 0; i < rows.get(0).size(); i++) {
                int longestString =0;
                for (ArrayList<String> row : rows) {
                    longestString=Math.max(longestString,row.get(i).length());
                    for (ArrayList<String> row1 : rows) {
                        row1.set(i,fitStringToLength(row1.get(i),longestString));
                    }
                }
            }
            int tableLength = 0;
            for (ArrayList<String> row : rows) {
                StringBuilder tableRow = new StringBuilder("|| ");
                for (String entry : row) {
                    tableRow.append(entry+"  || ");
                }
                tableLength = tableRow.length();
                System.out.println(fitStringToLength(TABLE_HORIZONTAL_SEPERATOR,tableLength));
                System.out.println(tableRow.toString());
            }
            System.out.println(fitStringToLength(TABLE_HORIZONTAL_SEPERATOR,tableLength));


        } finally {
            rs.close();
        }
    }

    public static String fitStringToLength(String string, int length){
        if (string.length()>= length-1){
            return string.substring(0,length-1);
        }else{
            StringBuilder stringBuilder = new StringBuilder(string);
            for (int i = 0; i < length - stringBuilder.length(); i++) {
                stringBuilder.append(" ");
            }
            return stringBuilder.toString();
        }
    }
}
