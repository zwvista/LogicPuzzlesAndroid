package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
open class LoopyHelpActivity : GameHelpActivity<LoopyGame?, LoopyDocument?, LoopyGameMove?, LoopyGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LoopyDocument? = null
    override fun doc(): LoopyDocument {
        return document!!
    }
}