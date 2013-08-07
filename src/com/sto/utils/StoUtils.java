package com.sto.utils;

import android.database.DatabaseUtils;
import android.util.Log;
import com.sto.entity.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 4/24/13
 */
public class StoUtils {

    public static final String FIELD_SEPARATOR = "\\|\\|";
    public static final String VALUE_SEPARATOR = "\\|";

    public static List<String> getAllInsertStatements(InputStream insertStatementStream) {
        List<String> statements = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(insertStatementStream));
            String strLine;
            String insertInto = bufferedReader.readLine();
            while ((strLine = bufferedReader.readLine()) != null) {
                if (!strLine.startsWith("insert")) {
                    try {
                        String xfields = findXfields(strLine);
                        String parsedFields = parseXFields(xfields);
                        strLine = strLine.replace(xfields, parsedFields);
                        statements.add(insertInto + " " + strLine);
                    } catch (Exception e) {
                        Log.e("!!!!!!!!!!", strLine);

                    }
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

    static String findXfields(String s) {
        int start = s.indexOf("',") + 4;
        int end = s.indexOf("',", start);
        String xfields = s.substring(start, end);
        return xfields;
    }

    static String parseXFields(String xfields) {
        xfields = xfields.replace("|||", "||");
        String telephone = "";
        String longitude = "";
        String latitude = "";
        String site = "";
        String time = "";
        String washType = "";
        String[] splitedXfields = xfields.split(FIELD_SEPARATOR);
        for (String fields : splitedXfields) {
            String[] split = fields.split(VALUE_SEPARATOR);
            if (fields.startsWith("tel")) {
                if (split.length > 1) {
                    telephone = split[1];
                }
            } else if (fields.startsWith("crd1")) {
                if (split.length > 1) {
                    longitude = split[1];
                }
            } else if (fields.startsWith("crd2")) {
                if (split.length > 1) {
                    latitude = split[1];
                }
            } else if (fields.startsWith("time")) {
                if (split.length > 1) {
                    time = split[1];
                }
            } else if (fields.startsWith("www")) {
                if (split.length > 1) {
                    site = split[1];
                }
            } else if (fields.startsWith("wash")) {
                if (split.length > 1) {
                    washType = split[1];
                }
            }
        }
        return telephone + "','" + longitude + "','" + latitude + "','" + time + "','" + site + "','" + washType;
    }

    public static List<Category> parseCategory(String categoryString) {
        List<Category> result = new ArrayList<>();
        String[] categories = categoryString.split(",");
        for (String category : categories) {
            Category stoCategory = Category.getCategoryById(Integer.parseInt(category));
            if (stoCategory != null) {
                result.add(stoCategory);
            }
        }
        return result;
    }

}
