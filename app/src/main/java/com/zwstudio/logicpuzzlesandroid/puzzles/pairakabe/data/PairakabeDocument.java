package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeObject;

import org.androidannotations.annotations.EBean;

@EBean
public class PairakabeDocument extends GameDocument<PairakabeGame, PairakabeGameMove> {
    protected void saveMove(PairakabeGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public PairakabeGameMove loadMove(MoveProgress rec) {
        return new PairakabeGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = PairakabeObject.objTypeFromString(rec.strValue1);
        }};
    }
}
