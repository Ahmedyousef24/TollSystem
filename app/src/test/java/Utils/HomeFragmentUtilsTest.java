package Utils;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;


public class HomeFragmentUtilsTest {

    private HomeFragmentUtils utils;

    @Before
    public void setUp() throws Exception {
        utils = new HomeFragmentUtils();
    }

    @Test
    public void getDueDate() {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

        String dueDate = simpleDateFormat.format(c.getTime());

        assertEquals(dueDate,utils.getDueDate());

    }

    @Test
    public void getGreetingsGoodMorning() {
        Calendar c = Calendar.getInstance();

        c.set(299,20,11,11,0);

        assertEquals("Good Morning" , utils.getGreetings(c));
    }

    @Test
    public void getGreetingsGoodEvening(){

        Calendar c = Calendar.getInstance();

        c.set(299,20,11,16,0);

        assertEquals("Good Evening" , utils.getGreetings(c));
    }

    @Test
    public void getGreetingsGoodAfternoon(){

        Calendar c = Calendar.getInstance();

        c.set(299,20,11,12,0);

        assertEquals("Good Afternoon" , utils.getGreetings(c));
    }
    @Test
    public void getGreetingsGoodNight(){

        Calendar c = Calendar.getInstance();

        c.set(299,20,11,21,0);

        assertEquals("Good Night" , utils.getGreetings(c));
    }

    @Test
    public void getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
        String date = simpleDateFormat.format(c.getTime());

        assertEquals(date, utils.getDate());
    }

}