package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data.FenceSentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
open class FenceSentinelsMainActivity : GameMainActivity<FenceSentinelsGame?, FenceSentinelsDocument?, FenceSentinelsGameMove?, FenceSentinelsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceSentinelsDocument? = null
    override fun doc(): FenceSentinelsDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        FenceSentinelsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FenceSentinelsGameActivity_.intent(this).start()
    }
}