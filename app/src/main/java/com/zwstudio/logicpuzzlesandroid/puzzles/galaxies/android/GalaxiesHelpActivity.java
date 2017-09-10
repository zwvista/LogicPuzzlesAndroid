package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.data.GalaxiesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class GalaxiesHelpActivity extends GameHelpActivity<GalaxiesGame, GalaxiesDocument, GalaxiesGameMove, GalaxiesGameState> {
    @Bean
    protected GalaxiesDocument document;
    public GalaxiesDocument doc() {return document;}
}
