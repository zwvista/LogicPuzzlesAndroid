package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.data.FourMeNotDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain.FourMeNotGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class FourMeNotOptionsActivity : GameOptionsActivity<FourMeNotGame?, FourMeNotDocument?, FourMeNotGameMove?, FourMeNotGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FourMeNotDocument? = null
    override fun doc(): FourMeNotDocument {
        return document!!
    }
}