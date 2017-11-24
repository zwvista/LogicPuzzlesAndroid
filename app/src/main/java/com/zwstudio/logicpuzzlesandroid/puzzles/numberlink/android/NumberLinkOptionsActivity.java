package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.data.NumberLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain.NumberLinkGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class NumberLinkOptionsActivity extends GameOptionsActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState> {
    @Bean
    protected NumberLinkDocument document;
    public NumberLinkDocument doc() {return document;}

    protected void onDefault() {}
}
