package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data.BoxItAroundDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class BoxItAroundMainActivity extends GameMainActivity<BoxItAroundGame, BoxItAroundDocument, BoxItAroundGameMove, BoxItAroundGameState> {
    public BoxItAroundDocument doc() {return app.boxitaroundDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        BoxItAroundOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        BoxItAroundGameActivity_.intent(this).start();
    }
}
