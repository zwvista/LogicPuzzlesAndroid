package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.data.NumberPathDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain.NumberPathGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class NumberPathHelpActivity extends GameHelpActivity<NumberPathGame, NumberPathDocument, NumberPathGameMove, NumberPathGameState> {
    @Bean
    protected NumberPathDocument document;
    public NumberPathDocument doc() {return document;}
}
