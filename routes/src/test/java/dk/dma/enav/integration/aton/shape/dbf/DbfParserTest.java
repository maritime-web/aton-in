package dk.dma.enav.integration.aton.shape.dbf;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Steen on 08-02-2016.
 */
public class DbfParserTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testName() throws Exception {
        List<Map<String, Object>> ref = DbfParser.parse(getClass().getClassLoader().getResourceAsStream("Fyr_Gronland_13jan2016.dbf"));

        ref.forEach(System.out::println);

    }
}