package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

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
    public int getMarkerOption() {
        String o = gameProgress().option1;
        return o == null ? 0 : Integer.parseInt(o);
    }
    public void setMarkerOption(GameProgress rec, int o) {
        rec.option1 = String.valueOf(o);
    }
}
