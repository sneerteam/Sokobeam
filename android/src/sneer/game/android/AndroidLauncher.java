package sneer.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import sneer.game.sokabota.core.Sokabota;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SokabotaApp(initialGame()), config);
	}

    private Sokabota initialGame() {
        return new Sokabota(
                "WWWWWWWWWWWWWWWWWWWWWWWWWWW",
                "W>      W        W        W",
                "W  \\     W        W        W",
                "W  2    W        W        W",
                "WWWWWWWbW        W        W",
                "W                         W",
                "W                         W",
                "W            /            W",
                "W     \\                   W",
                "W                         W",
                "W           WWWWWWWWWWWWbbW",
                "W      1    W           bbW",
                "W           W             W",
                "W A         W b           W",
                "WWWWWWWWWWWWWW!WWWWWWWWWWWW"
        );
    }
}
