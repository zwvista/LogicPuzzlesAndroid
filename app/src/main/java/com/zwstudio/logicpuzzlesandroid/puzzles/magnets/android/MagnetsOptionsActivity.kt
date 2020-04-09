package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class MagnetsOptionsActivity : GameOptionsActivity<MagnetsGame?, MagnetsDocument?, MagnetsGameMove?, MagnetsGameState?>() {
    @JvmField
    @Bean
    protected var document: MagnetsDocument? = null
    override fun doc(): MagnetsDocument {
        return document!!
    }
}