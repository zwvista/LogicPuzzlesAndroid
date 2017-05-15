package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.data.BoxItAgainDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class BoxItAgainHelpActivity extends GameHelpActivity<BoxItAgainGame, BoxItAgainDocument, BoxItAgainGameMove, BoxItAgainGameState> {
    public BoxItAgainDocument doc() {return app.boxitagainDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
