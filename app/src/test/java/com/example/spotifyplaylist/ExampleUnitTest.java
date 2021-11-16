package com.example.spotifyplaylist;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.spotifyplaylist.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //Does not yet test anything but to help make sure login is working
    @Test
    public void loginCheck(){
        String username = "freeAccount";
        String password = "password";
        assertFalse(false);
    }

}
