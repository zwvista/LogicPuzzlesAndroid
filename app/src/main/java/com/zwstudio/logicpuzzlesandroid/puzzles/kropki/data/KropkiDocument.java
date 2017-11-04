package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class KropkiDocument extends GameDocument<KropkiGame, KropkiGameMove> {
    protected void saveMove(KropkiGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj;
    }
    public KropkiGameMove loadMove(MoveProgress rec) {
        return new KropkiGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = rec.intValue1;
        }};
    }
}
