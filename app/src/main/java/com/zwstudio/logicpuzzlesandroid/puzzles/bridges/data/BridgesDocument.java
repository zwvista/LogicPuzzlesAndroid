package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class BridgesDocument extends GameDocument<BridgesGame, BridgesGameMove> {
    protected void saveMove(BridgesGameMove move, MoveProgress rec) {
        rec.row = move.pFrom.row;
        rec.col = move.pFrom.col;
        rec.row2 = move.pTo.row;
        rec.col2 = move.pTo.col;
    }
    public BridgesGameMove loadMove(MoveProgress rec) {
        return new BridgesGameMove() {{
            pFrom = new Position(rec.row, rec.col);
            pTo = new Position(rec.row2, rec.col2);
        }};
    }
}
