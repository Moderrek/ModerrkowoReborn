package pl.moderr.moderrkowo.reborn.opening.data;

import pl.moderr.moderrkowo.reborn.opening.OpeningChest;

import java.util.Map;

public class UserChestStorage {

    private Map<String, OpeningChest> userStorage;

    public UserChestStorage(Map<String, OpeningChest> userStorage){
        this.userStorage = userStorage;
    }

    public Map<String, OpeningChest> getUserStorage() {
        return userStorage;
    }

    public void setUserStorage(Map<String, OpeningChest> userStorage) {
        this.userStorage = userStorage;
    }
}
