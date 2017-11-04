package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class KropkiMainActivity extends GameMainActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState> {
    @Bean
    protected KropkiDocument document;
    public KropkiDocument doc() {return document;}

    @Click
    void btnOptions() {
        KropkiOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        KropkiGameActivity_.intent(this).start();
    }
}
