package com.zwstudio.logicpuzzlesandroid.puzzles.parks.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class ParksMainActivity extends MainActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState> {
    public ParksDocument doc() {return app.parksDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        ParksOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        ParksGameActivity_.intent(this).start();
    }
}
