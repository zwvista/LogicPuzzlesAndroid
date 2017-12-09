package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class DisconnectFourDocument extends GameDocument<DisconnectFourGame, DisconnectFourGameMove> {
    protected void saveMove(DisconnectFourGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public DisconnectFourGameMove loadMove(MoveProgress rec) {
        return new DisconnectFourGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = DisconnectFourObject.values()[rec.intValue1];
        }};
    }
}
