package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TatamiDocument extends GameDocument<TatamiGame, TatamiGameMove> {
    protected void saveMove(TatamiGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public TatamiGameMove loadMove(MoveProgress rec) {
        return new TatamiGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TatamiObject.values()[rec.intValue1];
        }};
    }
}
