package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.gui.tasks.scheduler.EditIntervalMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class AllCommandsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public AllCommandsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-commands")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title("§9§lTask commands")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();
        pagination.setItems(getAllCommands(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));

        String[] addItemLore = new String[]{ "",
                "§7Add a new command that will be",
                "§7executed on your specified schedule" };
        ItemStack addItem = Items.generateItem("§bAdd command", XMaterial.ANVIL, addItemLore);
        ClickableItem clickableAddItem = ClickableItem.of(addItem, e -> {
            TaskCommand taskCommand = new TaskCommand(task, UUID.randomUUID(), "my command", Gender.CONSOLE);
            task.addCommand(taskCommand);
            new EditCommandMenu(taskCommand).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableAddItem);

        String[] selectModeLore = new String[]{ "",
                "§7The commands don't need to be executed all",
                "§7at once. You can decide between one of the",
                "§7following selection modes:",
                "",
                "§7  - §eALL: §7Execute all commands at once",
                "",
                "§7  - §eORDERED: §7Execute the commands one by one.",
                "§7    This works best if you specify seconds. It will",
                "§7    execute command 1 the first time the task is executed,",
                "§7    then it will pick command 2 on next execution and so on.",
                "",
                "§7  - §eRANDOM: §7Same as above, but will pick a random",
                "§7    command at each execution.",
                "",
                "§7  - §eINTERVAL: §7Execute each command in order with",
                "§7    an interval in between.",
                "§7    See ORDERED for more information",
                "",
                "§7Current mode: §e" + task.getCommandExecutionMode().toString(),
                task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL) ?
                        "§7Current interval: §e" + task.getCommandExecutionInterval().toString() : "",
                "",
                "§aLeft-click to switch",
                task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL) ? "§aRight-click to change interval" : ""
        };
        ItemStack selectModeItem = Items.generateItem("§bExecution mode", XMaterial.DIAMOND_SHOVEL, selectModeLore);
        ClickableItem clickableSelectModeItem = ClickableItem.of(selectModeItem, e -> {
            if (e.getClick().equals(ClickType.LEFT)) {
                task.switchCommandExecutionMode();
                this.INVENTORY.open(player);
                return;
            }

            new EditIntervalMenu(task.getCommandExecutionInterval(), e2 -> new AllCommandsMenu(task).INVENTORY.open(player)).INVENTORY.open(player);
        });
        contents.set(0, 8, clickableSelectModeItem);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem[] getAllCommands(Player p) {
        List<TaskCommand> commands = task.getCommands();

        if (commands == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[commands.size()];

        for (int i = 0; i < items.length; i++) {
            TaskCommand taskCommand = commands.get(i);
            String command = taskCommand.getCommand();
            String[] lore = new String[]{
                    "",
                    "§7Gender: §e" + taskCommand.getGender(),
                    "",
                    "§aLeft-Click to edit",
                    "§cRight-Click to delete"
            };
            ItemStack item = Items.generateItem("§b" + command, XMaterial.COMMAND_BLOCK_MINECART, lore);

            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new EditCommandMenu(taskCommand).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    task.removeCommand(taskCommand);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
