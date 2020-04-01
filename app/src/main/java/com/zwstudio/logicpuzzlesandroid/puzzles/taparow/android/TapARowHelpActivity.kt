package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data.TapARowDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
class TapARowHelpActivity : GameHelpActivity<TapARowGame?, TapARowDocument?, TapARowGameMove?, TapARowGameState?>() {
    @Bean
    protected var document: TapARowDocument? = null
    override fun doc(): TapARowDocument {
        return document!!
    }
}