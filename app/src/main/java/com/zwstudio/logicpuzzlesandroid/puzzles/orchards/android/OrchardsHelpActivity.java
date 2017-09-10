package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data.OrchardsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class OrchardsHelpActivity extends GameHelpActivity<OrchardsGame, OrchardsDocument, OrchardsGameMove, OrchardsGameState> {
    @Bean
    protected OrchardsDocument document;
    public OrchardsDocument doc() {return document;}
}
