package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.LevelProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.home.data.DBHelper;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.data.BootyIslandDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.data.BoxItAgainDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data.BoxItAroundDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.data.BusySeasDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data.LighthousesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data.PairakabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.data.PowerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.data.ProductSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data.SentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.ormlite.annotations.OrmLiteDao;


@EApplication
public class LogicPuzzlesApplication extends Application {

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HomeGameProgress, Integer> daoHomeGameProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<GameProgress, Integer> daoGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LevelProgress, Integer> daoLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MoveProgress, Integer> daoMoveProgress;

    @Bean
    public HomeDocument homeDocument;
    @Bean
    public AbcDocument abcDocument;
    @Bean
    public BattleShipsDocument battleshipsDocument;
    @Bean
    public BootyIslandDocument bootyislandDocument;
    @Bean
    public BoxItAgainDocument boxitagainDocument;
    @Bean
    public BoxItAroundDocument boxitaroundDocument;
    @Bean
    public BoxItUpDocument boxitupDocument;
    @Bean
    public BridgesDocument bridgesDocument;
    @Bean
    public BusySeasDocument busyseasDocument;
    @Bean
    public CloudsDocument cloudsDocument;
    @Bean
    public FenceItUpDocument fenceitupDocument;
    @Bean
    public HitoriDocument hitoriDocument;
    @Bean
    public LightenUpDocument lightenupDocument;
    @Bean
    public LighthousesDocument lighthousesDocument;
    @Bean
    public LineSweeperDocument linesweeperDocument;
    @Bean
    public LoopyDocument loopyDocument;
    @Bean
    public MagnetsDocument magnetsDocument;
    @Bean
    public MasyuDocument masyuDocument;
    @Bean
    public MosaikDocument mosaikDocument;
    @Bean
    public NeighboursDocument neighboursDocument;
    @Bean
    public NurikabeDocument nurikabeDocument;
    @Bean
    public PairakabeDocument pairakabeDocument;
    @Bean
    public ParksDocument parksDocument;
    @Bean
    public PowerGridDocument powergridDocument;
    @Bean
    public ProductSentinelsDocument productsentinelsDocument;
    @Bean
    public RoomsDocument roomsDocument;
    @Bean
    public SkyscrapersDocument skyscrapersDocument;
    @Bean
    public SentinelsDocument sentinelsDocument;
    @Bean
    public SlitherLinkDocument slitherlinkDocument;
    @Bean
    public SumscrapersDocument sumscrapersDocument;
    @Bean
    public TentsDocument tentsDocument;

    @Bean
    public SoundManager soundManager;

    @Override
    public void onCreate() {
        super.onCreate();

        abcDocument.init();
        battleshipsDocument.init();
        bootyislandDocument.init();
        boxitagainDocument.init();
        boxitaroundDocument.init();
        boxitupDocument.init();
        bridgesDocument.init();
        busyseasDocument.init();
        cloudsDocument.init();
        fenceitupDocument.init();
        hitoriDocument.init();
        lightenupDocument.init();
        lighthousesDocument.init();
        linesweeperDocument.init();
        loopyDocument.init();
        magnetsDocument.init();
        masyuDocument.init();
        mosaikDocument.init();
        neighboursDocument.init();
        nurikabeDocument.init();
        pairakabeDocument.init();
        parksDocument.init();
        powergridDocument.init();
        productsentinelsDocument.init();
        roomsDocument.init();
        sentinelsDocument.init();
        skyscrapersDocument.init();
        slitherlinkDocument.init();
        sumscrapersDocument.init();
        tentsDocument.init();

        soundManager.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        soundManager.doUnbindService();
        OpenHelperManager.releaseHelper();
    }
}
