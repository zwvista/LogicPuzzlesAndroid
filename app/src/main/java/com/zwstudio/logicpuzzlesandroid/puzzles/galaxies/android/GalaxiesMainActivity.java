package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.data.GalaxiesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class GalaxiesMainActivity extends GameMainActivity<GalaxiesGame, GalaxiesDocument, GalaxiesGameMove, GalaxiesGameState> {
    @Bean
    protected GalaxiesDocument document;
    public GalaxiesDocument doc() {return document;}

    @Click
    void btnOptions() {
        GalaxiesOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        GalaxiesGameActivity_.intent(this).start();
    }
}
