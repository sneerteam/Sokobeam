package sneer.game.android;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import sneer.game.sokabota.core.BeamCrossing;
import sneer.game.sokabota.core.Box;
import sneer.game.sokabota.core.ExitDoor;
import sneer.game.sokabota.core.Gun;
import sneer.game.sokabota.core.Mirror;
import sneer.game.sokabota.core.Player;
import sneer.game.sokabota.core.Sokabota;
import sneer.game.sokabota.core.Wall;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.round;

public class SokabotaApp extends ApplicationAdapter {
    private final Sokabota game;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Tiles tiles;
    private BitmapFont font;
    private SpriteBatch batch;
    private TiledMapTileLayer gameLayer;
    private TiledMapTileLayer vbeamLayer;
    private TiledMapTileLayer hbeamLayer;
    private Sound laserSound;
    private Sound boxExplosionSound;
    private Sound fryingSound;

    public SokabotaApp(Sokabota game) {
        this.game = game;
    }

    @Override
    public void create () {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, cols(), rows());
        camera.update();

        font = new BitmapFont();
        batch = new SpriteBatch();

        {
            tiles = new Tiles();
            map = new TiledMap();
            MapLayers layers = map.getLayers();
            layers.add(backgroundLayer());
            vbeamLayer = newTiledMapLayer();
            layers.add(vbeamLayer);
            hbeamLayer = newTiledMapLayer();
            layers.add(hbeamLayer);
            gameLayer = newTiledMapLayer();
            updateScreen();
            layers.add(gameLayer);
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

        laserSound = newSound("data/Laser.wav");
        boxExplosionSound = newSound("data/box_exploding.wav");
        fryingSound = newSound("data/fritacao_dos_personagens.wav");

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean tap(float x, float y, int count, int button) {

                int numberOfBoxesBefore = numberOfBoxes();
                int gunsFiringBefore = numberOfGunsFiring();
                int deadPlayersBefore = numberOfDeadPlayers();

                game.tap(1, tileRow(y), tileCol(x));

                triggerBoxExplosions(numberOfBoxesBefore);
                triggerFryingSound(deadPlayersBefore);
                triggerLaserSound(gunsFiringBefore);

                updateScreen();

                return false;
            }
        }));
    }

    private void triggerFryingSound(int deadPlayersBefore) {
        playMany(numberOfDeadPlayers() - deadPlayersBefore, fryingSound);
    }

    private void triggerLaserSound(int gunsFiringBefore) {
        if (numberOfGunsFiring() > gunsFiringBefore) laserSound.play();
    }

    private void triggerBoxExplosions(int numberOfBoxesBefore) {
        playMany(numberOfBoxesBefore - numberOfBoxes(), boxExplosionSound);
    }

    private void playMany(int times, final Sound sound) {
        for (int i = 0; i < times; ++i)
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    sound.play();
                }
            }, i * .1f);
    }

    private int numberOfGunsFiring() {
        return numberOfGunsFiring(game.scene);
    }

    private int numberOfGunsFiring(Square[][] scene) {
        int total = 0;
        for (int i = 0; i < scene.length; i++)
            for (int j = 0; j < scene[i].length; j++)
                if (isGunFiring(scene[i][j].thing))
                    total++;
        return total;
    }

    private int numberOfDeadPlayers() {
        return numberOfDeadPlayers(game.scene);
    }

    private int numberOfDeadPlayers(Square[][] scene) {
        int total = 0;
        for (int i = 0; i < scene.length; i++)
            for (int j = 0; j < scene[i].length; j++)
                if (isDeadPlayer(scene[i][j].thing))
                    total++;
        return total;
    }

    private boolean isDeadPlayer(Thing thing) {
        return thing instanceof Player && ((Player)thing).isDead();
    }

    private int numberOfBoxes() {
        return numberOfBoxes(game.scene);
    }

    private int numberOfBoxes(Square[][] scene) {
        int total = 0;
        for (int i = 0; i < scene.length; i++)
            for (int j = 0; j < scene[i].length; j++)
                if (scene[i][j].thing instanceof Box)
                    total++;
        return total;
    }

    private boolean isGunFiring(Thing thing) {
        return thing instanceof Gun && ((Gun)thing).isOn;
    }

    private int tileCol(float x) {
        return floor(x / Gdx.graphics.getWidth() * cols());
    }

    private int tileRow(float y) {
        return floor(y / Gdx.graphics.getHeight() * rows());
    }

    private void updateScreen() {
        Square[][] scene = game.scene;
        updateCells(scene);
    }

    private void updateCells(Square[][] scene) {
        for (int x = 0; x < cols(); ++x) {
            for (int y = 0; y < rows(); ++y) {
                Thing t = scene[y][x].thing;
                int row = rows() - y - 1;
                updateGameCell(x, row, t);
                updateBeamCell(x, row, t);
            }
        }
    }

    private void updateGameCell(int col, int row, Thing t) {
        TiledMapTileLayer.Cell cell = tiles.forThing(t);
        gameLayer.setCell(col, row, cell);
    }

    private void updateBeamCell(int col, int row, Thing t) {
        vbeamLayer.setCell(col, row, verticalBeamCellFor(t));
        hbeamLayer.setCell(col, row, horizontalBeamCellFor(t));
    }

    private TiledMapTileLayer.Cell horizontalBeamCellFor(Thing t) {
        if (t instanceof BeamCrossing) {
            BeamCrossing b = (BeamCrossing)t;
            return b.hasHorizontalBeam ? tiles.beamH : null;
        }
        return null;
    }

    private TiledMapTileLayer.Cell verticalBeamCellFor(Thing t) {
        if (t instanceof BeamCrossing) {
            BeamCrossing b = (BeamCrossing)t;
            return b.hasVerticalBeam ? tiles.beamV : null;
        }
        return null;
    }

    private TiledMapTileLayer backgroundLayer() {
        TiledMapTileLayer bgLayer = newOpaqueTiledMapLayer();
        for (int x = 0; x < cols(); ++x)
            for (int y = 0; y < rows(); ++y)
                bgLayer.setCell(x, y, tiles.bg);
        return bgLayer;
    }

    private TiledMapTileLayer newOpaqueTiledMapLayer() {
        TiledMapTileLayer layer = newTiledMapLayer();
        layer.setOpacity(1);
        return layer;
    }

    private TiledMapTileLayer newTiledMapLayer() {
        return new TiledMapTileLayer(cols(), rows(), 16, 16);
    }

    private Sound newSound(String path) {
        return Gdx.audio.newSound(internalFile(path));
    }

    private int rows() {
        return game.scene.length;
    }

    private int cols() {
        if (game.scene.length < 1) throw new AssertionError();
        return game.scene[0].length;
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + "(" + cols() + ", " + rows() + ")", 10, 20);
        batch.end();
    }

    static class Tiles {
        final TileLoader tiles = new TileLoader();
        final TiledMapTileLayer.Cell exitDoor = tiles.cell(0);
        final TiledMapTileLayer.Cell corpse = tiles.cell(1);
        final TiledMapTileLayer.Cell beamH = beamH();
        final TiledMapTileLayer.Cell beamV = beamV();
        final TiledMapTileLayer.Cell bg = tiles.cell(12);
        final TiledMapTileLayer.Cell box = tiles.cell(23);
        final TiledMapTileLayer.Cell wall = tiles.cell(24);
        final TiledMapTileLayer.Cell mirrorLeft = tiles.cell(16);
        final TiledMapTileLayer.Cell mirrorRight = tiles.cell(18);
        final TiledMapTileLayer.Cell gunLeft = tiles.animated(1, 4, 5);
        final TiledMapTileLayer.Cell gunDown = tiles.animated(1, 6, 7);
        final TiledMapTileLayer.Cell gunRight = tiles.animated(1, 8, 9);
        final TiledMapTileLayer.Cell gunUp = tiles.animated(1, 10, 11);
        final TiledMapTileLayer.Cell[] players = new TiledMapTileLayer.Cell[] {
                tiles.cell(19),
                tiles.cell(20),
                tiles.cell(21),
                tiles.cell(22)
        };

        public TiledMapTileLayer.Cell forGun(Gun gun) {
            switch (gun.direction) {
                case UP: return gunUp;
                case DOWN: return gunDown;
                case LEFT: return gunLeft;
                case RIGHT: return gunRight;
                default: throw new IllegalStateException();
            }
        }

        public TiledMapTileLayer.Cell forPlayer(Player player) {
            return player.isDead() ? corpse : players[player.number() - 1];
        }

        public TiledMapTileLayer.Cell forThing(Thing t) {
            if (t instanceof Gun)
                return forGun((Gun) t);
            if (t instanceof Wall)
                return wall;
            if (t instanceof Player)
                return forPlayer((Player) t);
            if (t instanceof Mirror)
                return forMirror((Mirror)t);
            if (t instanceof ExitDoor)
                return exitDoor;
            if (t instanceof Box)
                return box;
            return null;
        }

        private TiledMapTileLayer.Cell forMirror(Mirror mirror) {
            return mirror.orientation.equals("/") ? mirrorRight : mirrorLeft;
        }

        private TiledMapTileLayer.Cell beamH() {
            boolean vertical = false;
            return tiles.textureAnimation(tiles.beamH, vertical, 4);
        }

        private TiledMapTileLayer.Cell beamV() {
            boolean vertical = true;
            return tiles.textureAnimation(tiles.beamV, vertical, 4);
        }
    }

    static class TileLoader {
        public final Texture beamH;
        public final Texture beamV;
        private TextureRegion[] tiles;

        TileLoader() {
            Texture texture = new Texture(internalFile("todos.png"));
            TextureRegion[][] splitTiles = TextureRegion.split(texture, 16, 16);
            if (splitTiles.length != 1) throw new AssertionError();
            tiles = splitTiles[0];
            beamH = textureWithRepeat("beam-h.png");
            beamV = textureWithRepeat("beam-v.png");
        }

        private Texture textureWithRepeat(String path) {
            Texture t = new Texture(internalFile(path));
            t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            return t;
        }

        public TiledMapTileLayer.Cell cell(int col) {
            return staticFor(texture(col));
        }

        private TextureRegion texture(int col) {
            return tiles[col];
        }

        public TiledMapTileLayer.Cell animated(float period, int... cols) {
            return cellForTile(animatedTile(period, cols));
        }

        public TiledMapTileLayer.Cell animated(float period, TextureRegion... frames) {
            return cellForTile(new AnimatedTiledMapTile(period, staticTiles(frames)));
        }

        public AnimatedTiledMapTile animatedTile(float period, int... cols) {
            return new AnimatedTiledMapTile(period, staticTiles(cols));
        }

        public TiledMapTileLayer.Cell textureAnimation(Texture t, boolean vertical, int frameCount) {
            TextureRegion[] frames = new TextureRegion[frameCount];
            int delta = round((vertical ? t.getHeight() : t.getWidth()) / (float)frameCount);
            for (int i = 0; i < frameCount; i++) {
                frames[i] = vertical
                        ? new TextureRegion(t, 0, delta * i, t.getWidth(), t.getHeight())
                        : new TextureRegion(t, delta * i, 0, t.getWidth(), t.getHeight());
            }
            return animated(1.0f / frameCount, frames);
        }

        private Array<StaticTiledMapTile> staticTiles(int[] cols) {
            Array<StaticTiledMapTile> array = new Array<>(cols.length);
            for (int i = 0; i < cols.length; i++) {
                array.add(staticTile(cols[i]));
            }
            return array;
        }

        private Array<StaticTiledMapTile> staticTiles(TextureRegion[] textures) {
            Array<StaticTiledMapTile> array = new Array<>(textures.length);
            for (int i = 0; i < textures.length; i++) {
                array.add(staticTileFor(textures[i]));
            }
            return array;
        }

        private StaticTiledMapTile staticTile(int col) {
            return staticTileFor(texture(col));
        }

        private TiledMapTileLayer.Cell staticFor(TextureRegion bgTexture) {
            return cellForTile(staticTileFor(bgTexture));
        }

        private TiledMapTileLayer.Cell cellForTile(TiledMapTile tile) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tile);
            return cell;
        }

        private StaticTiledMapTile staticTileFor(TextureRegion bgTexture) {
            return new StaticTiledMapTile(bgTexture);
        }
    }

    private static FileHandle internalFile(String path) {
        return Gdx.files.internal(path);
    }
}
