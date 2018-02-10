package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsObject;

import org.androidannotations.annotations.EBean;

@EBean
public class WallSentinelsDocument extends GameDocument<WallSentinelsGame, WallSentinelsGameMove> {
    protected void saveMove(WallSentinelsGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public WallSentinelsGameMove loadMove(MoveProgress rec) {
        return new WallSentinelsGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = WallSentinelsObject.objFromString(rec.strValue1);
        }};
    }
}
