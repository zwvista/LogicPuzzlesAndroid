package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class HolidayIslandHintObject extends HolidayIslandObject {
    public HintState state = HintState.Normal;
    public int tiles;
    public String objAsString() {
        return "hint";
    }
}
