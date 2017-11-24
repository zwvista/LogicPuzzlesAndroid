package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data.NumberLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class NumberLinkHelpActivity extends GameHelpActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState> {
    @Bean
    protected NumberLinkDocument document;
    public NumberLinkDocument doc() {return document;}
}
