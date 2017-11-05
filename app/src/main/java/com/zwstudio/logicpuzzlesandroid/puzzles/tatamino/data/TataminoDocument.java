package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain.TataminoGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class TataminoDocument extends GameDocument<TataminoGame, TataminoGameMove> {
    protected void saveMove(TataminoGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.strValue1 = String.valueOf(move.obj);
    }
    public TataminoGameMove loadMove(MoveProgress rec) {
        return new TataminoGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.strValue1.charAt(0);
        }};
    }
}
