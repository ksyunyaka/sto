package com.sto.utils;

import com.sto.entity.Category;

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

    private static final Pattern coordinatePattern = Pattern.compile("crd1\\W(\\d+\\p{Punct}\\d+)\\W\\Wcrd2\\W(\\d+\\p{Punct}\\d+)");

    public static float[] parseCoordinates(String s) {
        float[] result = new float[2];
        Matcher m = coordinatePattern.matcher(s);
        if (m.find()) {
            String y = m.group(1);
            String x = m.group(2);
            result[0] = Float.parseFloat(x);
            result[1] = Float.parseFloat(y);
        }
        return result;
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
