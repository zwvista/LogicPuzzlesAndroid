package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data.FenceSentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
class FenceSentinelsHelpActivity : GameHelpActivity<FenceSentinelsGame?, FenceSentinelsDocument?, FenceSentinelsGameMove?, FenceSentinelsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceSentinelsDocument? = null
    override fun doc(): FenceSentinelsDocument {
        return document!!
    }
}