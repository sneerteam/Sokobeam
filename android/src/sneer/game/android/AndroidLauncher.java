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
                "WWWWWWWWW",
                "W   V  !W",
                "W > 1 < W",
                "W \\ A / W",
                "WWWWWWWWW");
    }
}
