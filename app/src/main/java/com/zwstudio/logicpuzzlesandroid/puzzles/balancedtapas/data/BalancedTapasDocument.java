package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasObject;

import org.androidannotations.annotations.EBean;

@EBean
public class BalancedTapasDocument extends GameDocument<BalancedTapasGame, BalancedTapasGameMove> {
    protected void saveMove(BalancedTapasGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objTypeAsString();
    }
    public BalancedTapasGameMove loadMove(MoveProgress rec) {
        return new BalancedTapasGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = BalancedTapasObject.objTypeFromString(rec.strValue1);
        }};
    }
}
