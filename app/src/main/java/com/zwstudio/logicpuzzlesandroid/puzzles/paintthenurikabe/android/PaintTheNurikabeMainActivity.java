package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.data.PaintTheNurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain.PaintTheNurikabeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class PaintTheNurikabeMainActivity extends GameMainActivity<PaintTheNurikabeGame, PaintTheNurikabeDocument, PaintTheNurikabeGameMove, PaintTheNurikabeGameState> {
    @Bean
    protected PaintTheNurikabeDocument document;
    public PaintTheNurikabeDocument doc() {return document;}

    @Click
    void btnOptions() {
        PaintTheNurikabeOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        PaintTheNurikabeGameActivity_.intent(this).start();
    }
}
