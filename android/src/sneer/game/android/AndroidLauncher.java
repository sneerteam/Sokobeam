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
		Sokabota game = initialGame(); //demoGame();
        initialize(new SokabotaApp(game), config);
	}

    private Sokabota initialGame() {
        return new Sokabota(
                "              Y            ",
                "                           ",
                "                           ",
                "Y                          ",
                " bbbbbbbb<                 ",
                "bbbbbbbbbb                 ",
                "                           ",
                "                           ",
                "   bbbb bb  WWWWWWWWWWWWWWb",
                "bbbbbbbYbbbWWWWWWWWWWWWWWbb",
                "bbb      bbWWbb  b   2     ",
                "        bbbWWbbb  b        ",
                "bbbb  bbbb WWbbbb /bb      ",
                " /  1   bb WWWWWWW b       ",
                "        bb WWW! bb         "
        );
    }

    private Sokabota demoGame() {
        return new Sokabota(
                "WWWWWWWWWWWWWWWWW",
                "W/ b     Y      W",
                "W  /     b    Y W",
                "WY 1     b   W  W",
                "W  Y           <W",
                "W>       Y    /WW",
                "Wbbbb    /      W",
                "WWWWWWWWWWWWWWW!W"
        );
    }
}


    /*
              Fase 1 (box&door learning)

        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "             1             ",
        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "                           ",
        "              b            ",
        "             W!W           "

                Fase 2 : laser learning

                "1                          ",
                "                           ",
                "                           ",
                "                           ",
                "  >                        ",
                "                           ",
                "                         bb",
                "                         b!",
                "                         bb",
                "                           ",
                "                           ",
                "                           ",
                "          b                ",
                "                           ",
                "                           "

           Fase 3  (mirror&laser learning)

                "                          b",
                "                         b!",
                "                         bb",
                "                           ",
                "                           ",
                "                           ",
                "                           ",
                "       /                   ",
                "                           ",
                "                           ",
                "                           ",
                "                    1      ",
                "                           ",
                "                           ",
                " \\             <W           "

            Fase semi-complexa

                "1 bb      W bbb            ",
                "bbb       W bbbbb     Y  bb",
                "b bb    > W    bb     bbbbb",
                "bb        W                ",
                "          Wb           2   ",
                "          bb               ",
                "WWWWWWWWWWWb               ",
                "!W                         ",
                "bW                         ",
                "                           ",
                "                           ",
                "                           ",
                "                           ",
                "    / Y                    ",
                "                           "

            Coop fase test

                "              Y            ",
                "                           ",
                "                           ",
                "Y                          ",
                " bbbbbbbb<                 ",
                "bbbbbbbbbb                 ",
                "                           ",
                "                           ",
                "    bbb bb  WWWWWWWWWWWWWWb",
                "bbbbbbbYbbbWWWWWWWWWWWWWWbb",
                "bbb      bbWWBb  b   2     ",
                "        bbbWWbbb  b        ",
                "bbbb  bbbb WWbbbb /bb      ",
                " /  1   bb WWWWWWW b       ",
                "        bb WWW! bb         "

      */