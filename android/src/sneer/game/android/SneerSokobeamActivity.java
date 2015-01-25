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

    private PartnerMessenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new SokabotaApp(initialGame()), config);

        messenger = startMessenger();
    }

    private PartnerMessenger startMessenger() {
        return new PartnerMessenger(this, new PartnerMessenger.Listener() {

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
    }

    @Override
    protected void onDestroy() {
        if (messenger != null) messenger.dispose();
        super.onDestroy();
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

    private void toast(final String message) {
        Toast.makeText(SneerSokobeamActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
