package sneer.game.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import sneer.android.ui.PartnerMessenger;
import sneer.game.sokabota.core.Sokabota;


public class SneerSokobeamActivity extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_sokobeam);
        new PartnerMessenger(this, new PartnerMessenger.Listener() {

            @Override
            public void onPartnerName(String s) {
                toast("onPartnerName(" + s + ")");
            }

            @Override
            public void onMessageToPartner(Object o) {
                toast("onMessageToPartner(" + o + ")");
            }

            @Override
            public void onMessageFromPartner(Object o) {
                toast("onMessageFromPartner" + o + ")");
            }

            @Override
            public void update() {

            }
        });

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new SokabotaApp(initialGame()), config);
    }

    private Sokabota initialGame() {
        return new Sokabota(
                "WWWWWWWWWWWWWWWW",
                "W  b     b     W",
                "W        b     W",
                "W  1     b   2 W",
                "W  \\          <W",
                "W>       /    bW",
                "WWWWWWWWWWWWWW!W"
        );
    }

    private void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multiplayer_sokobeam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
