package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.data.FourMeNotDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class FourMeNotOptionsActivity extends GameOptionsActivity<FourMeNotGame, FourMeNotDocument, FourMeNotGameMove, FourMeNotGameState> {
    @Bean
    protected FourMeNotDocument document;
    public FourMeNotDocument doc() {return document;}
}
