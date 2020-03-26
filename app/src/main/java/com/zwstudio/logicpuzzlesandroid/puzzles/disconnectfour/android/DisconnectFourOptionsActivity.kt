package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.data.DisconnectFourDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
open class DisconnectFourOptionsActivity : GameOptionsActivity<DisconnectFourGame?, DisconnectFourDocument?, DisconnectFourGameMove?, DisconnectFourGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: DisconnectFourDocument? = null
    override fun doc(): DisconnectFourDocument {
        return document!!
    }
}