package pl.moderr.moderrkowo.reborn.opening.data;

import pl.moderr.moderrkowo.reborn.utils.WeightedList;

import java.util.ArrayList;

public interface ModerrCase {
    String name();
    String guiName();
    String description();
    WeightedList<ModerrCaseItem> randomList();
    ArrayList<ModerrCaseItem> itemList();
}
