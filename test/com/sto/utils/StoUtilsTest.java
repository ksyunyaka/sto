package com.sto.utils;


import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Egor
 * Date: 05.08.13
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class StoUtilsTest {

    @Test
    public void parseXfieldsTest(){
        String first = "crd1|30.462539||crd2|50.37327||tel|-";
        String result = StoUtils.parseXFields(first);
        assertEquals("-','30.462539','50.37327','','','", result);

        String second = "tel|(063) 633 01 03||crd1|30.488987||crd2|50.460871||time|Круглосуточно||www|||wash_type|обычная/бесконтактная";
        result = StoUtils.parseXFields(second);
        assertEquals("(063) 633 01 03','30.488987','50.460871','Круглосуточно','','обычная/бесконтактная", result);

        String third = "tel|(067) 208-89-51||crd1|||crd2|||time|Некруглосуточно||www|||wash_type|обычная/бесконтактная,";
        assertEquals("(067) 208-89-51','','','Некруглосуточно','','обычная/бесконтактная,", StoUtils.parseXFields(third));
    }

    @Test
    public void findXfieldsTest(){
        String s = "(201, 'Киев, ул. Глубочицкая, 16', 'tel|(063) 633 01 03||crd1|30.488987||crd2|50.460871||time|Круглосуточно||www|||wash_type|обычная/бесконтактная', 'Водограй', 'Киев, ул. Глубочицкая, 16', '10' );";
        assertEquals("tel|(063) 633 01 03||crd1|30.488987||crd2|50.460871||time|Круглосуточно||www|||wash_type|обычная/бесконтактная", StoUtils.findXfields(s));

    }

    @Test
    public void t(){
        String expected = "(139, 'г. Киев, ул. Генерала Наумова, 36', 'crd1|30.356563||crd2|50.473754', 'БАРС', 'г. Киев, ул. ''Генерала Наумова'', 36', '12,40,41' );";
        String tested="(139, 'г. Киев, ул. Генерала Наумова, 36', 'crd1|30.356563||crd2|50.473754', 'БАРС', 'г. Киев, ул. \'Генерала Наумова\', 36', '12,40,41' );";
        assertEquals(expected, tested.replaceAll("\\\'", "''"));

    }
}
