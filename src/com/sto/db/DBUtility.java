package com.sto.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 3/1/13
 */
public class DBUtility {


    public static List<String> getAllInsertStatements(InputStream insertStatementStream) {
        List<String> statements = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(insertStatementStream));
            String strLine;
            String insertInto = bufferedReader.readLine();
            while ((strLine = bufferedReader.readLine()) != null) {
                if (!strLine.startsWith("INSERT")) {
                    if (strLine.charAt(strLine.length() - 1) == ',') {
                        strLine = strLine.substring(0, strLine.length() - 1);
                    }
                    statements.add(insertInto + " " + strLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {

            }
        }
        return statements;
    }
}
