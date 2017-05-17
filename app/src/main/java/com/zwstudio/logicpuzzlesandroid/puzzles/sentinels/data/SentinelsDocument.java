package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class SentinelsDocument extends GameDocument<SentinelsGame, SentinelsGameMove> {
    protected void saveMove(SentinelsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public SentinelsGameMove loadMove(MoveProgress rec) {
        return new SentinelsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = SentinelsObject.objFromString(rec.strValue1);
        }};
    }
}
