package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove;

import org.androidannotations.annotations.EBean;

@EBean
public class TennerGridDocument extends GameDocument<TennerGridGame, TennerGridGameMove> {
    protected void saveMove(TennerGridGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public TennerGridGameMove loadMove(MoveProgress rec) {
        return new TennerGridGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
