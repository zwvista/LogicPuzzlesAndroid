package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain.BridgesGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
open class BridgesHelpActivity : GameHelpActivity<BridgesGame?, BridgesDocument?, BridgesGameMove?, BridgesGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BridgesDocument? = null
    override fun doc(): BridgesDocument {
        return document!!
    }
}