package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.data.RobotCrosswordsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class RobotCrosswordsHelpActivity extends GameHelpActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState> {
    @Bean
    protected RobotCrosswordsDocument document;
    public RobotCrosswordsDocument doc() {return document;}
}
