package pl.moderr.moderrkowo.reborn.villagers.data;

public interface IQuestItemPay extends IQuestItem {

    int getCount();
    @Override
    default String getQuestItemPrefix() {
        return "Zapłać";
    }

}

