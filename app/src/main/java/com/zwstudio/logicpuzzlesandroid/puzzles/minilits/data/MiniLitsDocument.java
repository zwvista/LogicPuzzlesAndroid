package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class MiniLitsDocument extends GameDocument<MiniLitsGame, MiniLitsGameMove> {
    protected void saveMove(MiniLitsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public MiniLitsGameMove loadMove(MoveProgress rec) {
        return new MiniLitsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = MiniLitsObject.objFromString(rec.strValue1);
        }};
    }
}
