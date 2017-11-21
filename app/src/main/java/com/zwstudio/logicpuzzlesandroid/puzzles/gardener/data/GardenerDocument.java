package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class GardenerDocument extends GameDocument<GardenerGame, GardenerGameMove> {
    protected void saveMove(GardenerGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = move.obj.objAsString();
    }
    public GardenerGameMove loadMove(MoveProgress rec) {
        return new GardenerGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = GardenerObject.objFromString(rec.strValue1);
        }};
    }
}
