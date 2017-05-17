package com.zwstudio.logicpuzzlesandroid.puzzles.lits.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain.LitsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class LitsDocument extends GameDocument<LitsGame, LitsGameMove> {
    protected void saveMove(LitsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public LitsGameMove loadMove(MoveProgress rec) {
        return new LitsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = LitsObject.objFromString(rec.strValue1);
        }};
    }
}
