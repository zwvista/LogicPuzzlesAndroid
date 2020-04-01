package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class FenceItUpMainActivity : GameMainActivity<FenceItUpGame?, FenceItUpDocument?, FenceItUpGameMove?, FenceItUpGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceItUpDocument? = null
    override fun doc(): FenceItUpDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        FenceItUpOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        FenceItUpGameActivity_.intent(this).start()
    }
}