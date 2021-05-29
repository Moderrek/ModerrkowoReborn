package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;

public class OpeningData {

    private OpeningChestReward reward;
    private Inventory inv;
    private int taskId;

    @Contract(pure = true)
    public OpeningData(OpeningChestReward reward, Inventory inv, int taskId){
        this.inv = inv;
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

    public OpeningChestReward getReward() {
        return reward;
    }

    public void setReward(OpeningChestReward reward) {
        this.reward = reward;
    }
}
