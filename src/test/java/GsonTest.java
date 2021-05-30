import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseEnum;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItem;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItemType;
import pl.moderr.moderrkowo.reborn.opening.data.UserChestStorage;

public class GsonTest {

    public static void main(String args[]){
        UserChestStorage userChestStorage = new UserChestStorage("{\"Key|TEST\":{\"amount\":1,\"type\":\"Key\",\"chestType\":\"TEST\"}}");
        userChestStorage.addKey(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA),false);
        System.out.println(userChestStorage.toString());
    }

}
