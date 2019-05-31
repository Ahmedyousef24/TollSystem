package Utils;

import android.content.Context;
import android.location.Geocoder;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class HomeFragmentUtilsInstrumentedTest {

    private HomeFragmentUtils utils;
    private Context context;

    @Before
    public void setUp() throws Exception {
        utils = new HomeFragmentUtils();
        context = InstrumentationRegistry.getContext();
    }

    @Test
    public void getAddress() {
        double lat = 40.750580;
        double lon = -73.993584;
        String address = "Oxtorgsgatan 26, 553 17 Jönköping, Sweden";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        assertEquals(address, utils.getAddress(57.7815, 14.1562, geocoder));

    }
}