package sneer.game.android;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

import sneer.game.sokabota.core.Gun;
import sneer.game.sokabota.core.Player;
import sneer.game.sokabota.core.Sokabota;
import sneer.game.sokabota.core.Wall;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

public class SokabotaApp extends ApplicationAdapter {
    private final Sokabota game;
    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private TileLoader tiles;
    private BitmapFont font;
    private SpriteBatch batch;

    public SokabotaApp(Sokabota game) {
        this.game = game;
    }

    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, cols(), rows());
        camera.update();

        font = new BitmapFont();
        batch = new SpriteBatch();

        {
            tiles = new TileLoader();

            TiledMapTileLayer.Cell bgCell = tiles.cell(12);
            TiledMapTileLayer.Cell wall = tiles.cell(24);
            TiledMapTileLayer.Cell gunLeft = tiles.animated(1, 4, 5);
            TiledMapTileLayer.Cell gunDown = tiles.animated(1, 6, 7);
            TiledMapTileLayer.Cell gunRight = tiles.animated(1, 8, 9);
            TiledMapTileLayer.Cell gunUp = tiles.animated(1, 10, 11);
            TiledMapTileLayer.Cell[] players = new TiledMapTileLayer.Cell[] {
                    tiles.cell(19),
                    tiles.cell(20),
                    tiles.cell(21),
                    tiles.cell(22)
            };

            map = new TiledMap();
            MapLayers layers = map.getLayers();
            {
                TiledMapTileLayer bgLayer = newTiledMapLayer();
                bgLayer.setOpacity(1);
                for (int x = 0; x < cols(); ++x) {
                    for (int y = 0; y < rows(); ++y) {
                        bgLayer.setCell(x, y, bgCell);
                    }
                }
                layers.add(bgLayer);
            }

            {
                Square[][] scene = game.scene;
                TiledMapTileLayer gameLayer = newTiledMapLayer();
                for (int x = 0; x < cols(); ++x) {
                    for (int y = 0; y < rows(); ++y) {
                        Thing t = scene[y][x].thing;
                        if (t instanceof Gun) {
                            gameLayer.setCell(x, y, selectGun((Gun)t, gunLeft, gunRight, gunUp, gunDown));
                        } else if (t instanceof Wall) {
                            gameLayer.setCell(x, y, wall);
                        } else if (t instanceof Player) {
                            gameLayer.setCell(x, y, players[((Player)t).number() - 1]);
                        }
                    }
                }
                layers.add(gameLayer);
            }
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

    }

    private TiledMapTileLayer.Cell selectGun(Gun t, TiledMapTileLayer.Cell gunLeft, TiledMapTileLayer.Cell gunRight, TiledMapTileLayer.Cell gunUp, TiledMapTileLayer.Cell gunDown) {
        switch (t.direction) {
            case UP: return gunUp;
            case DOWN: return gunDown;
            case LEFT: return gunLeft;
            case RIGHT: return gunRight;
            default: throw new IllegalStateException();
        }
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
            Array<StaticTiledMapTile> array = new Array<>(cols.length);
            for (int i = 0; i < cols.length; i++) {
                array.add(staticTile(cols[i]));
            }
            return new AnimatedTiledMapTile(period, array);
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
