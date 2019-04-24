package pl.agh.cs.io.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.agh.cs.io.Rules;
import pl.agh.cs.io.State;
import pl.agh.cs.io.Time;

public class StatsController {


    public Rules rules;

    @FXML
    private TableView<Data> data;
    @FXML private TableColumn<Data, String> name;
    @FXML private TableColumn<Data, Integer> foregroundTime;
    @FXML private TableColumn<Data, Integer> backgroundTime;

    @FXML
    public void initialize() {

    }


    public void setRules(Rules rules){
        this.rules = rules;
        data.setEditable(true);
        for(String key : rules.getRules().keySet()){
            Data dataForKey = new Data(key, rules.getRules().get(key).getTimes().stream().
                    filter(time -> time.getType().equals(State.FG)).map(Time::getAmount).reduce(0.0, Double::sum),
                    rules.getRules().get(key).getTimes().stream().filter(time -> time.getType().equals(State.BG)).
                            map(Time::getAmount).reduce(0.0, Double::sum));
            data.getItems().add(dataForKey);
        }
    }



    public class Data{
        private SimpleStringProperty name;
        private SimpleDoubleProperty foregroundTime;
        private SimpleDoubleProperty backgroundTime;

        public Data() {
            this("", 0.0,0.0);
        }

        public Data(String name, Double foregroundTime, Double backgroundTime){
            this.name = new SimpleStringProperty(name);
            this.backgroundTime = new SimpleDoubleProperty(backgroundTime);
            this.foregroundTime = new SimpleDoubleProperty(foregroundTime);
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

        public double getForegroundTime() {
            return foregroundTime.get();
        }

        public SimpleDoubleProperty foregroundTimeProperty() {
            return foregroundTime;
        }

        public void setForegroundTime(int foregroundTime) {
            this.foregroundTime.set(foregroundTime);
        }

        public double getBackgroundTime() {
            return backgroundTime.get();
        }

        public SimpleDoubleProperty backgroundTimeProperty() {
            return backgroundTime;
        }

        public void setBackgroundTime(int backgroundTime) {
            this.backgroundTime.set(backgroundTime);
        }
    }

}
