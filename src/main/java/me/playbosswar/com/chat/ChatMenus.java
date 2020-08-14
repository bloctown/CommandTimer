package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Gender;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.*;
import org.bukkit.entity.Player;

public class ChatMenus {
    public static void openTimerMenu(Player p, String name) {
        CommandTimer timer = CommandsManager.getCommandTimer(name);

        if (timer == null) {
            Messages.sendMessageToPlayer(p, "&cWe could not find this timer");
            return;
        }

        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer: " + timer.getName()), 5, 1));

        menu.add(new TextElement("Seconds: ", 5, 3));
        IncrementalElement secondsInc = new IncrementalElement(60, 3, 1, Integer.MAX_VALUE, timer.getSeconds());
        secondsInc.value.setChangeCallback(state -> {
            CommandsManager.changeCommandtimerData(p, timer.getName(), "seconds", state.getCurrent().toString());
        });
        menu.add(secondsInc);

        String[] genders = new String[3];

        genders[0] = "CONSOLE";
        genders[1] = "OPERATOR";
        genders[2] = "PLAYER";

        int defaultIndex = 0;
        if (timer.getGender().equals(Gender.OPERATOR)) {
            defaultIndex = 1;
        }
        if (timer.getGender().equals(Gender.PLAYER)) {
            defaultIndex = 2;
        }

        menu.add(new TextElement("Gender: ", 5, 5));
        VerticalSelectorElement genderSelector = new VerticalSelectorElement(50, 5, defaultIndex, genders);
        genderSelector.value.setChangeCallback(state -> {
            CommandsManager.changeCommandtimerData(p, timer.getName(), "gender", genders[state.getCurrent()]);
        });
        menu.add(genderSelector);

        menu.add(new TextElement("Execution chance: ", 5, 8));
        double existingValue = timer.getRandom() * 20;
        NumberSliderElement executionSlider = new NumberSliderElement(100, 8, 20, (int) existingValue);
        executionSlider.value.setChangeCallback(state -> {
            double pourcentage = state.getCurrent() * 0.05;

            CommandsManager.changeCommandtimerData(p, timer.getName(), "random", pourcentage + "");
        });
        menu.add(executionSlider);

        menu.add(new TextElement("Execute for each user: ", 5, 9));
        BooleanElement perUser = new BooleanElement(150, 9, timer.getExecutePerUser());
        perUser.value.setChangeCallback(state -> CommandsManager.changeCommandtimerData(p, timer.getName(), "executePerUser", state.getCurrent().toString()));
        menu.add(perUser);

        menu.add(new TextElement("Use Minecraft time: ", 5, 10));
        BooleanElement useMinecraftTime = new BooleanElement(150, 10, timer.getUseMinecraftTime());
        useMinecraftTime.value.setChangeCallback(state -> CommandsManager.changeCommandtimerData(p, timer.getName(), "useMinecraftTime", state.getCurrent().toString()));
        menu.add(useMinecraftTime);

        menu.add(new ButtonElement(5, 12, Messages.colorize("&a&l[Commands]"), player -> {
            menu.close(player);
            CommandsChatMenu.openCommandsMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(90, 12, Messages.colorize("&a&l[Hours]"), player -> {
            menu.close(player);
            TimesChatMenu.openTimesMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(170, 12, Messages.colorize("&a&l[Days]"), player -> {
            menu.close(player);
            DaysChatMenu.openDaysMenu(player, timer.getName());
        }));

        menu.add(new ButtonElement(5, 15, Messages.colorize("&c[Close]"), menu::close));

        menu.openFor(p);
    }
}