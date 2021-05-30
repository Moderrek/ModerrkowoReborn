package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItem;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItemTemp;

public class OpeningData {

    private ModerrCaseItemTemp reward;
    private String name;
    private Inventory inv;
    private int taskId;

    @Contract(pure = true)
    public OpeningData(ModerrCaseItemTemp reward, String name, Inventory inv, int taskId){
        this.inv = inv;
        this.name = name;
        this.taskId = taskId;
    }


    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public ModerrCaseItemTemp getReward() {
        return reward;
    }

    public void setReward(ModerrCaseItemTemp reward) {
        this.reward = reward;
    }

    public String name() {
        return name;
    }
}
