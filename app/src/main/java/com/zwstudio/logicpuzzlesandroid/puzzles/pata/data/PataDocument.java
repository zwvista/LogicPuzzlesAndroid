package com.zwstudio.logicpuzzlesandroid.puzzles.pata.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class PataDocument extends GameDocument<PataGame, PataGameMove> {
    protected void saveMove(PataGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public PataGameMove loadMove(MoveProgress rec) {
        return new PataGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = PataObject.objTypeFromString(rec.strValue1);
        }};
    }
}
