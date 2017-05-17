package com.zwstudio.logicpuzzlesandroid.puzzles.domino.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.data.DominoDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameState;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class DominoOptionsActivity extends GameOptionsActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState> {
    public DominoDocument doc() {return app.dominoDocument;}
}
