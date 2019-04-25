package pl.agh.cs.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;


import  org.mockito.*;

public class RuleTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handleFG()  throws Exception {
        //before;
        Rule rule = new Rule("c:/xd");
        Field field = rule.getClass().getDeclaredField("time");
        field.setAccessible(true);
        Time timeMock= mock(Time.class);
        field.set(rule,timeMock);

        //test
        rule.handle(State.FG);

        //after
        verify(timeMock).addFgTime();

    }
    @Test
    public void handleBG()  throws Exception {
        //before
        Rule rule = new Rule("c:/xd");
        Field field = rule.getClass().getDeclaredField("time");
        field.setAccessible(true);
        Time timeMock= mock(Time.class);
        field.set(rule,timeMock);

        //test
        rule.handle(State.BG);

        //after
        verify(timeMock).addBgTime();
    }

}