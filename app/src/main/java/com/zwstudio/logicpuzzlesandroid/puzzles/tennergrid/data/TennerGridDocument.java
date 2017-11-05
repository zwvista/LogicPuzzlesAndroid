package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TennerGridDocument extends GameDocument<TennerGridGame, TennerGridGameMove> {
    protected void saveMove(TennerGridGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public TennerGridGameMove loadMove(MoveProgress rec) {
        return new TennerGridGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TennerGridObject.values()[rec.intValue1];
        }};
    }
}
