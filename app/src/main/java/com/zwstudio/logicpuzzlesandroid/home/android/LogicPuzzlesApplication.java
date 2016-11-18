package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.zwstudio.logicpuzzlesandroid.home.data.DBHelper;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersMoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkLevelProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkMoveProgress;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.ormlite.annotations.OrmLiteDao;


@EApplication
public class LogicPuzzlesApplication extends Application {

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HomeGameProgress, Integer> daoLogicGamesGameProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<AbcGameProgress, Integer> daoAbcGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<AbcLevelProgress, Integer> daoAbcLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<AbcMoveProgress, Integer> daoAbcMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<BridgesGameProgress, Integer> daoBridgesGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<BridgesLevelProgress, Integer> daoBridgesLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<BridgesMoveProgress, Integer> daoBridgesMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<CloudsGameProgress, Integer> daoCloudsGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<CloudsLevelProgress, Integer> daoCloudsLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<CloudsMoveProgress, Integer> daoCloudsMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HitoriGameProgress, Integer> daoHitoriGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HitoriLevelProgress, Integer> daoHitoriLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HitoriMoveProgress, Integer> daoHitoriMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LightenUpGameProgress, Integer> daoLightenUpGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LightenUpLevelProgress, Integer> daoLightenUpLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LightenUpMoveProgress, Integer> daoLightenUpMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LineSweeperGameProgress, Integer> daoLineSweeperGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LineSweeperLevelProgress, Integer> daoLineSweeperLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LineSweeperMoveProgress, Integer> daoLineSweeperMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MasyuGameProgress, Integer> daoMasyuGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MasyuLevelProgress, Integer> daoMasyuLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MasyuMoveProgress, Integer> daoMasyuMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<NurikabeGameProgress, Integer> daoNurikabeGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<NurikabeLevelProgress, Integer> daoNurikabeLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<NurikabeMoveProgress, Integer> daoNurikabeMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SkyscrapersGameProgress, Integer> daoSkyscrapersGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SkyscrapersLevelProgress, Integer> daoSkyscrapersLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SkyscrapersMoveProgress, Integer> daoSkyscrapersMoveProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SlitherLinkGameProgress, Integer> daoSlitherLinkGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SlitherLinkLevelProgress, Integer> daoSlitherLinkLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<SlitherLinkMoveProgress, Integer> daoSlitherLinkMoveProgress;

    @Bean
    public HomeDocument homeDocument;
    @Bean
    public AbcDocument abcDocument;
    @Bean
    public BridgesDocument bridgesDocument;
    @Bean
    public CloudsDocument cloudsDocument;
    @Bean
    public HitoriDocument hitoriDocument;
    @Bean
    public LightenUpDocument lightenupDocument;
    @Bean
    public LineSweeperDocument linesweeperDocument;
    @Bean
    public MasyuDocument masyuDocument;
    @Bean
    public NurikabeDocument nurikabeDocument;
    @Bean
    public SkyscrapersDocument skyscrapersDocument;
    @Bean
    public SlitherLinkDocument slitherlinkDocument;

    @Bean
    public SoundManager soundManager;

    @Override
    public void onCreate() {
        super.onCreate();

        abcDocument.init();
        bridgesDocument.init();
        cloudsDocument.init();
        hitoriDocument.init();
        lightenupDocument.init();
        linesweeperDocument.init();
        masyuDocument.init();
        nurikabeDocument.init();
        skyscrapersDocument.init();
        slitherlinkDocument.init();

        soundManager.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        soundManager.doUnbindService();
        OpenHelperManager.releaseHelper();
    }
}
