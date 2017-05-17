package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class RoomsOptionsActivity extends GameOptionsActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState> {
    @Bean
    protected RoomsDocument document;
    public RoomsDocument doc() {return document;}
}
