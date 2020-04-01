package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class LightenUpOptionsActivity : GameOptionsActivity<LightenUpGame?, LightenUpDocument?, LightenUpGameMove?, LightenUpGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: LightenUpDocument? = null
    override fun doc(): LightenUpDocument {
        return document!!
    }
}