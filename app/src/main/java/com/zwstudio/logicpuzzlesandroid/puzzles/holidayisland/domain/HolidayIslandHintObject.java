package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class HolidayIslandHintObject extends HolidayIslandObject {
    public HintState state = HintState.Normal;
    public int tiles;
    public String objAsString() {
        return "hint";
    }
}
