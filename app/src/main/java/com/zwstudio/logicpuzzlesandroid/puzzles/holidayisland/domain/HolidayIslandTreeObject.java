package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class HolidayIslandTreeObject extends HolidayIslandObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
