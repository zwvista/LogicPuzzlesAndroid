package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class HolidayIslandDocument extends GameDocument<HolidayIslandGame, HolidayIslandGameMove> {
    protected void saveMove(HolidayIslandGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public HolidayIslandGameMove loadMove(MoveProgress rec) {
        return new HolidayIslandGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = HolidayIslandObject.objFromString(rec.strValue1);
        }};
    }
}
