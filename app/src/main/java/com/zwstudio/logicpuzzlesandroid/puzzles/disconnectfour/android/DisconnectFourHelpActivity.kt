package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.data.DisconnectFourDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
class DisconnectFourHelpActivity : GameHelpActivity<DisconnectFourGame?, DisconnectFourDocument?, DisconnectFourGameMove?, DisconnectFourGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: DisconnectFourDocument? = null
    override fun doc() = document!!
}