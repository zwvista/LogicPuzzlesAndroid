package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data.FenceLitsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
open class FenceLitsHelpActivity : GameHelpActivity<FenceLitsGame?, FenceLitsDocument?, FenceLitsGameMove?, FenceLitsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceLitsDocument? = null
    override fun doc(): FenceLitsDocument {
        return document!!
    }
}