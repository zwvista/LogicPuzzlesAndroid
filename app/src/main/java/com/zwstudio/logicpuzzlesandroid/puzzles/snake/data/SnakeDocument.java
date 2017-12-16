package com.zwstudio.logicpuzzlesandroid.puzzles.snake.data;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeObject;

import org.androidannotations.annotations.EBean;

@EBean
public class SnakeDocument extends GameDocument<SnakeGame, SnakeGameMove> {
    protected void saveMove(SnakeGameMove move, MoveProgress rec) {
        rec.row = move.p.row;
        rec.col = move.p.col;
        rec.intValue1 = move.obj.ordinal();
    }
    public SnakeGameMove loadMove(MoveProgress rec) {
        return new SnakeGameMove() {{
            p = new Position(rec.row, rec.col);
            obj = SnakeObject.values()[rec.intValue1];
        }};
    }
}
