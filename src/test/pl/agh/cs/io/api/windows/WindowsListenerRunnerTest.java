package pl.agh.cs.io.api.windows;

import javafx.concurrent.ScheduledService;
import javafx.util.Duration;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import pl.agh.cs.io.Rules;
import pl.agh.cs.io.TimeCounterController;
import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class WindowsListenerRunnerTest {


    Consumer<OpenWindowsProcessesPerExeSnapshot> callback;
    @Before
    public void setUp() throws Exception {
        Rules rules = mock(Rules.class);
        TimeCounterController controller = mock(TimeCounterController.class);

        callback =
                Mockito.spy(snapshot -> {
                    rules.accept(snapshot);
                    Method method = null;
                    try {
                        method = snapshot.getClass().getDeclaredMethod("getForegroundWindowProcessIdsPerPath");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    method.setAccessible(true);
                    try {
                        controller.accept((ProcessIdsPerPath) method.invoke(snapshot), rules.getRulesCopy());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @Parameters(method = "generateSnapshot")
    public void run(Consumer<OpenWindowsProcessesPerExeSnapshot> callback) {
        ScheduledService<Void> service = mock(ScheduledService.class);
        service.setPeriod(Duration.seconds(3));
        service.start();
        verify(callback).accept(mock(OpenWindowsProcessesPerExeSnapshot.class));
    }

    public Consumer<OpenWindowsProcessesPerExeSnapshot> generateSnapshot() {
        return callback;
    }
}