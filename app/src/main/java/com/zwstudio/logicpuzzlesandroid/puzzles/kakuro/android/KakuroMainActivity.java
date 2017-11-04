package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data.KakuroDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class KakuroMainActivity extends GameMainActivity<KakuroGame, KakuroDocument, KakuroGameMove, KakuroGameState> {
    @Bean
    protected KakuroDocument document;
    public KakuroDocument doc() {return document;}

    @Click
    void btnOptions() {
        KakuroOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        KakuroGameActivity_.intent(this).start();
    }
}
