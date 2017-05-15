package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.data.MiniLitsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain.MiniLitsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class MiniLitsHelpActivity extends GameHelpActivity<MiniLitsGame, MiniLitsDocument, MiniLitsGameMove, MiniLitsGameState> {
    public MiniLitsDocument doc() {return app.minilitsDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
