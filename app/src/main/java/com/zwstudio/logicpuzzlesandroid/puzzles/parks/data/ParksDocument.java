package com.zwstudio.logicpuzzlesandroid.puzzles.parks.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class ParksDocument extends GameDocument<ParksGame, ParksGameMove> {
    protected void saveMove(ParksGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public ParksGameMove loadMove(MoveProgress rec) {
        return new ParksGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = ParksObject.objFromString(rec.strValue1);
        }};
    }
}
