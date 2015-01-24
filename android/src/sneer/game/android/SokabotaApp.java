package sneer.game.android;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

import sneer.game.sokabota.core.ExitDoor;
import sneer.game.sokabota.core.Gun;
import sneer.game.sokabota.core.Mirror;
import sneer.game.sokabota.core.Player;
import sneer.game.sokabota.core.Sokabota;
import sneer.game.sokabota.core.Wall;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

import static com.badlogic.gdx.math.MathUtils.floor;

public class SokabotaApp extends ApplicationAdapter {
    private final Sokabota game;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Tiles tiles;
    private BitmapFont font;
    private SpriteBatch batch;
    private TiledMapTileLayer gameLayer;

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
            gameLayer = newTiledMapLayer();
            updateGame();
            layers.add(gameLayer);
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean tap(float x, float y, int count, int button) {
                game.tap(1, tileRow(y), tileCol(x));
                updateGame();
                return false;
            }
        }));

    }

    private int tileCol(float x) {
        return floor(x / Gdx.graphics.getWidth() * cols());
    }

    private int tileRow(float y) {
        return floor(y / Gdx.graphics.getHeight() * rows());
    }

    private void updateGame() {
        Square[][] scene = game.scene;
        for (int x = 0; x < cols(); ++x) {
            for (int y = 0; y < rows(); ++y) {
                Thing t = scene[y][x].thing;
                TiledMapTileLayer.Cell cell = tiles.forThing(t);
                gameLayer.setCell(x, y, cell);
            }
        }
    }

    private TiledMapTileLayer backgroundLayer() {
        TiledMapTileLayer bgLayer = newTiledMapLayer();
        bgLayer.setOpacity(1);
        for (int x = 0; x < cols(); ++x) {
            for (int y = 0; y < rows(); ++y) {
                bgLayer.setCell(x, y, tiles.bg);
            }
        }
        return bgLayer;
    }

    private TiledMapTileLayer newTiledMapLayer() {
        return new TiledMapTileLayer(cols(), rows(), 16, 16);
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
        final TiledMapTileLayer.Cell bg = tiles.cell(12);
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
            return players[player.number() - 1];
        }

        public TiledMapTileLayer.Cell forThing(Thing t) {
            if (t instanceof Gun) {
                return forGun((Gun) t);
            } else if (t instanceof Wall) {
                return wall;
            } else if (t instanceof Player) {
                return forPlayer((Player) t);
            } else if (t instanceof Mirror) {
                return forMirror((Mirror)t);
            } else if (t instanceof ExitDoor) {
                return exitDoor;
            }
            return null;
        }

        private TiledMapTileLayer.Cell forMirror(Mirror mirror) {
            return mirror.direction == Direction.LEFT
                    ? mirrorLeft
                    : mirrorRight;
        }
    }

    static class TileLoader {
        private TextureRegion[] tiles;

        TileLoader() {
            Texture texture = new Texture(Gdx.files.internal("todos.png"));
            TextureRegion[][] splitTiles = TextureRegion.split(texture, 16, 16);
            if (splitTiles.length != 1) throw new AssertionError();
            tiles = splitTiles[0];
        }

        public TiledMapTileLayer.Cell cell(int col) {
            return staticFor(tiles[col]);
        }

        public TiledMapTileLayer.Cell animated(float period, int... cols) {
            return cellForTile(animatedTile(period, cols));
        }

        public AnimatedTiledMapTile animatedTile(float period, int... cols) {
            return new AnimatedTiledMapTile(period, staticTiles(cols));
        }

        private Array<StaticTiledMapTile> staticTiles(int[] cols) {
            Array<StaticTiledMapTile> array = new Array<>(cols.length);
            for (int i = 0; i < cols.length; i++) {
                array.add(staticTile(cols[i]));
            }
            return array;
        }

        private StaticTiledMapTile staticTile(int col) {
            return staticTileFor(tiles[col]);
        }

        private TiledMapTileLayer.Cell staticFor(TextureRegion bgTexture) {
            StaticTiledMapTile tile = staticTileFor(bgTexture);
            return cellForTile(tile);
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
}
