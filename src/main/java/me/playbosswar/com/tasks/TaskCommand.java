package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.WorldWeather;
import me.playbosswar.com.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskCommand {
    private final UUID uuid;
    private String command;
    private Gender gender;
    private transient Task task;
    private List<WorldWeather> weatherConditions = new ArrayList<>();
    private boolean executePerUser;

    public TaskCommand(Task task, UUID uuid, String command, Gender gender) {
        this.task = task;
        this.uuid = uuid;
        this.command = command;
        this.gender = gender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
        task.storeInstance();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
        task.storeInstance();
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public List<WorldWeather> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<WorldWeather> weatherConditions) {
        this.weatherConditions = weatherConditions;
        task.storeInstance();
    }

    public boolean isExecutePerUser() {
        return executePerUser;
    }

    public void setExecutePerUser(boolean executePerUser) {
        this.executePerUser = executePerUser;
        task.storeInstance();
    }

    public UUID getUuid() {
        return uuid;
    }
}
