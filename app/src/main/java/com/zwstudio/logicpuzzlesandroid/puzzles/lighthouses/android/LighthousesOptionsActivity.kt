package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data.LighthousesDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class LighthousesOptionsActivity : GameOptionsActivity<LighthousesGame?, LighthousesDocument?, LighthousesGameMove?, LighthousesGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LighthousesDocument? = null
    override fun doc(): LighthousesDocument {
        return document!!
    }
}