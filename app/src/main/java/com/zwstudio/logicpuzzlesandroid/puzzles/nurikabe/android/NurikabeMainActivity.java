package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.MainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NurikabeMainActivity extends MainActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState> {
    public NurikabeDocument doc() {return app.nurikabeDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        NurikabeOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NurikabeGameActivity_.intent(this).start();
    }
}
