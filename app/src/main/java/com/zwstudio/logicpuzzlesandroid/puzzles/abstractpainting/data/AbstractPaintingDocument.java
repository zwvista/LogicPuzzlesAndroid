package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain.AbstractPaintingObject;

import org.androidannotations.annotations.EBean;

/**
 * Created by zwvista on 2016/09/29.
 */

@EBean
public class AbstractPaintingDocument extends GameDocument<AbstractPaintingGame, AbstractPaintingGameMove> {
    protected void saveMove(AbstractPaintingGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public AbstractPaintingGameMove loadMove(MoveProgress rec) {
        return new AbstractPaintingGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = AbstractPaintingObject.values()[rec.intValue1];
        }};
    }
}
