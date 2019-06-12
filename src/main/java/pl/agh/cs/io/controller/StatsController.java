package pl.agh.cs.io.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.agh.cs.io.model.ActivityTime;
import pl.agh.cs.io.model.FileRules;
import pl.agh.cs.io.model.Rules;
import pl.agh.cs.io.model.WindowState;
import pl.agh.cs.io.model.FileActivityTime;


import java.util.concurrent.TimeUnit;

public class StatsController {


    public Rules rules;
    public FileRules fileRules;

    @FXML
    private TableView<Data> data;
    @FXML private TableColumn<Data, String> name;
    @FXML private TableColumn<Data, String> foregroundTime;
    @FXML private TableColumn<Data, String> backgroundTime;

    @FXML
    TableView<FileData> fileData;
    @FXML private TableColumn<FileData, String> fileName;
    @FXML private TableColumn<FileData, String> fileTime;

    @FXML
    public void initialize() {

    }


    public void setRules(FileRules fileRules, Rules rules) {
        this.rules = rules;
        data.setEditable(true);
        for (String key : rules.getRules().keySet()) {
            Data dataForKey = new Data(NameConverter.nameFromPath(key),
                    rules.getRules().get(key).getTimes().stream()
                            .filter(time -> time.getState().equals(WindowState.FOREGROUND))
                            .map(ActivityTime::getAmount)
                            .reduce(0.0, Double::sum),
                    rules.getRules().get(key).getTimes().stream()
                            .filter(time -> time.getState().equals(WindowState.BACKGROUND))
                            .map(ActivityTime::getAmount)
                            .reduce(0.0, Double::sum));
            data.getItems().add(dataForKey);
        }
        this.fileRules = fileRules;
        fileData.setEditable(true);
        for (String key : fileRules.getFileRulesObservableMap().keySet()) {
            FileData dataForKey = new FileData(NameConverter.nameFromPath(key),
                    fileRules.getFileRulesObservableMap().get(key).getTimes().stream()
                            .map(FileActivityTime::getAmount)
                            .reduce(0.0, Double::sum));
            fileData.getItems().add(dataForKey);
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

    public class FileData {
        private SimpleStringProperty fileName;
        private SimpleStringProperty fileTime;

        public FileData() {
            this("", 0.0);
        }

        public FileData(String name, Double fileTime) {
            this.fileName = new SimpleStringProperty(name);
            this.fileTime = new SimpleStringProperty(secondsToString(fileTime));
        }

        public String getFileName() {
            return fileName.get();
        }

        public SimpleStringProperty fileNameProperty() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName.set(fileName);
        }

        public String getFileTime() {
            return fileTime.get();
        }

        public SimpleStringProperty fileTimeProperty() {
            return fileTime;
        }

        public void setFileTime(String fileTime) {
            this.fileTime.set(fileTime);
        }
    }

    public static String secondsToString(Double seconds) {
        long longSeconds = seconds.longValue();
        StringBuilder builder = new StringBuilder();
        long uptime = TimeUnit.SECONDS.toDays(longSeconds);
        if (uptime > 0) {
            builder.append(TimeUnit.SECONDS.toDays(longSeconds)).append("d ");
            longSeconds = longSeconds - TimeUnit.DAYS.toSeconds(uptime);
        }
        uptime = TimeUnit.SECONDS.toHours(longSeconds);
        if (uptime > 0) {
            builder.append(TimeUnit.SECONDS.toHours(longSeconds)).append("h ");
            longSeconds = longSeconds - TimeUnit.HOURS.toSeconds(uptime);
        }
        uptime = TimeUnit.SECONDS.toMinutes(longSeconds);
        if (uptime > 0) {
            builder.append(TimeUnit.SECONDS.toMinutes(longSeconds)).append("m ");
            longSeconds = longSeconds - TimeUnit.MINUTES.toSeconds(uptime);
        }
        builder.append(longSeconds);
        builder.append("s");
        return builder.toString();
    }
}
