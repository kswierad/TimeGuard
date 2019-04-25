package pl.agh.cs.io.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.agh.cs.io.ActivityTime;
import pl.agh.cs.io.Rules;
import pl.agh.cs.io.WindowState;

import java.util.concurrent.TimeUnit;

public class StatsController {


    public Rules rules;

    @FXML
    private TableView<Data> data;
    @FXML private TableColumn<Data, String> name;
    @FXML private TableColumn<Data, String> foregroundTime;
    @FXML private TableColumn<Data, String> backgroundTime;

    @FXML
    public void initialize() {

    }


    public void setRules(Rules rules) {
        this.rules = rules;
        data.setEditable(true);
        for (String key : rules.getRules().keySet()) {
            Data dataForKey = new Data(key, rules.getRules().get(key).getTimes().stream().
                    filter(time -> time.getState().equals(WindowState.FOREGROUND)).map(ActivityTime::getAmount)
                        .reduce(0.0, Double::sum),
                    rules.getRules().get(key).getTimes().stream().filter(time -> time.getState()
                            .equals(WindowState.BACKGROUND)).map(ActivityTime::getAmount).reduce(0.0, Double::sum));
            data.getItems().add(dataForKey);
        }
    }



    public class Data {
        private SimpleStringProperty name;
        private SimpleStringProperty foregroundTime;
        private SimpleStringProperty backgroundTime;

        public Data() {
            this("", 0.0, 0.0);
        }

        public Data(String name, Double foregroundTime, Double backgroundTime) {
            this.name = new SimpleStringProperty(name);
            this.backgroundTime = new SimpleStringProperty(secondsToString(backgroundTime));
            this.foregroundTime = new SimpleStringProperty(secondsToString(foregroundTime));
        }

        private String secondsToString(Double seconds) {
            long longSeconds = seconds.longValue();
            StringBuilder builder = new StringBuilder();
            long uptime = TimeUnit.SECONDS.toDays(longSeconds);
            if (uptime > 0) {
                builder.append(TimeUnit.SECONDS.toDays(longSeconds)).append(":");
                longSeconds = longSeconds - TimeUnit.DAYS.toSeconds(uptime);
            }
            uptime = TimeUnit.SECONDS.toHours(longSeconds);
            if (uptime > 0) {
                builder.append(TimeUnit.SECONDS.toHours(longSeconds)).append(":");
                longSeconds = longSeconds - TimeUnit.HOURS.toSeconds(uptime);
            }
            uptime = TimeUnit.SECONDS.toMinutes(longSeconds);
            if (uptime > 0) {
                builder.append(TimeUnit.SECONDS.toMinutes(longSeconds)).append(":");
                longSeconds = longSeconds - TimeUnit.MINUTES.toSeconds(uptime);
            }
            builder.append(longSeconds);
            return builder.toString();
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getForegroundTime() {
            return foregroundTime.get();
        }

        public SimpleStringProperty foregroundTimeProperty() {
            return foregroundTime;
        }

        public void setForegroundTime(String foregroundTime) {
            this.foregroundTime.set(foregroundTime);
        }

        public String getBackgroundTime() {
            return backgroundTime.get();
        }

        public SimpleStringProperty backgroundTimeProperty() {
            return backgroundTime;
        }

        public void setBackgroundTime(String backgroundTime) {
            this.backgroundTime.set(backgroundTime);
        }
    }

}
