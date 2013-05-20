package com.sto.utils;

import android.content.Context;
import com.sto.db.DBController;
import com.sto.entity.STO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: egor
 * Date: 5/20/13
 */
public enum StoCache {

    INSTANCE;

    private static List<STO> allSTOEntities = new ArrayList<STO>();
    private static HashMap<String, STO> stoByMarkerId = new HashMap<String, STO>();
    private static HashMap<String, STO> stoByTitle = new HashMap<String, STO>();

    private static boolean isInitialized = false;


    public void initialize(Context context, InputStream insertStatementStream) {
        if (!isInitialized) {
            DBController dbController = new DBController(context, insertStatementStream);
            dbController.open();

            allSTOEntities = dbController.getAllSTOEntities();

            for (STO sto : allSTOEntities) {
                stoByTitle.put(sto.getTitle(), sto);
            }
            isInitialized = true;
        }
    }

    public List<STO> getAllSTOEntities() {
        return new ArrayList<>(allSTOEntities);
    }

    public STO getStoByMarkerId(String markerId) {
        return stoByMarkerId.get(markerId);
    }

    public STO getStoByTitle(String title) {
        return stoByTitle.get(title);
    }

    public void addStoByMarker(String markerId, STO sto) {
        stoByMarkerId.put(markerId, sto);
    }


}
