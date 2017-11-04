package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.data.RobotCrosswordsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class RobotCrosswordsOptionsActivity extends GameOptionsActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState> {
    @Bean
    protected RobotCrosswordsDocument document;
    public RobotCrosswordsDocument doc() {return document;}
}
