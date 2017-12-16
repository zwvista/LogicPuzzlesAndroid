package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoObject;

import org.androidannotations.annotations.EBean;

@EBean
public class TierraDelFuegoDocument extends GameDocument<TierraDelFuegoGame, TierraDelFuegoGameMove> {
    protected void saveMove(TierraDelFuegoGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public TierraDelFuegoGameMove loadMove(MoveProgress rec) {
        return new TierraDelFuegoGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TierraDelFuegoObject.objFromString(rec.strValue1);
        }};
    }
}
