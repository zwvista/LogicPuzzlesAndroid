package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.data.BoxItAgainDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain.BoxItAgainGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BoxItAgainMainActivity extends MainActivity<BoxItAgainGame, BoxItAgainDocument, BoxItAgainGameMove, BoxItAgainGameState> {
    public BoxItAgainDocument doc() {return app.boxitagainDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BoxItAgainOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BoxItAgainGameActivity_.intent(this).start();
    }
}
