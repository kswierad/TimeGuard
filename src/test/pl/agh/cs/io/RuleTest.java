package pl.agh.cs.io;

import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(JfxRunner.class)
public class RuleTest {

    Rule rule;

    @Before
    public void setUp() throws Exception {
        rule = new Rule("c:/xd");
        Field field = rule.getClass().getDeclaredField("prevState");
        field.setAccessible(true);
        field.set(rule, WindowState.BACKGROUND);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handleBgToFgRule() throws Exception{
        rule.handle(WindowState.FOREGROUND);
        ActivityTime time = rule.getTimes().get(rule.getTimes().size() -2);
        assertEquals(time.getState(), WindowState.BACKGROUND);
    }

    @Test
    public void handleCheckRestriction() throws Exception{
        rule.handle(WindowState.FOREGROUND);

        ActivityTime time = rule.getTimes().get(rule.getTimes().size() -2);
        RuleRestriction restriction = //mock(RuleRestriction.class);
                Mockito.spy(new RuleRestriction(WindowState.BACKGROUND, 3, ExceededUsageAction.NOTIFY));

        rule.setRestriction(restriction);
        Thread.sleep(3);

        Platform.runLater(
                () -> {
                    rule.handle(WindowState.FOREGROUND);
                    rule.removeRestriction();
                    rule.handle(WindowState.BACKGROUND);
                    verify(restriction, times(1)).checkRestriction();
                    verify(restriction).showAlert(rule);
                }
        );

    }



}