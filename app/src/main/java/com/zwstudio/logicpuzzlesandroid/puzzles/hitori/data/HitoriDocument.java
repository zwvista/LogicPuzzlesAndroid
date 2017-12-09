package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class HitoriDocument extends GameDocument<HitoriGame, HitoriGameMove> {
    protected void saveMove(HitoriGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public HitoriGameMove loadMove(MoveProgress rec) {
        return new HitoriGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = HitoriObject.values()[rec.intValue1];
        }};
    }
}
