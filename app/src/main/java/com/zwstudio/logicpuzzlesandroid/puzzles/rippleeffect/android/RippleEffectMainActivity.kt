package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data.RippleEffectDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class RippleEffectMainActivity : GameMainActivity<RippleEffectGame?, RippleEffectDocument?, RippleEffectGameMove?, RippleEffectGameState?>() {
    @Bean
    protected var document: RippleEffectDocument? = null
    override fun doc(): RippleEffectDocument {
        return document!!
    }

    @Click
    fun btnOptions() {
        RippleEffectOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        RippleEffectGameActivity_.intent(this).start()
    }
}