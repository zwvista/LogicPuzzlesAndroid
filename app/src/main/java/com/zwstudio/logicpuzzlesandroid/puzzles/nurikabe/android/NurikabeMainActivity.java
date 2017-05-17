package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class NurikabeMainActivity extends GameMainActivity<NurikabeGame, NurikabeDocument, NurikabeGameMove, NurikabeGameState> {
    @Bean
    protected NurikabeDocument document;
    public NurikabeDocument doc() {return document;}

    @Click
    void btnOptions() {
        NurikabeOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        NurikabeGameActivity_.intent(this).start();
    }
}
