package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class FourMeNotDocument extends GameDocument<FourMeNotGame, FourMeNotGameMove> {
    protected void saveMove(FourMeNotGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public FourMeNotGameMove loadMove(MoveProgress rec) {
        return new FourMeNotGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = FourMeNotObject.objFromString(rec.strValue1);
        }};
    }
}
