package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TataminoDocument extends GameDocument<TataminoGame, TataminoGameMove> {
    protected void saveMove(TataminoGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public TataminoGameMove loadMove(MoveProgress rec) {
        return new TataminoGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = TataminoObject.values()[rec.intValue1];
        }};
    }
}
