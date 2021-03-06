package jacob.pozaic.spaceinvaders;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import jacob.pozaic.spaceinvaders.game.SpaceInvaders;
import jacob.pozaic.spaceinvaders.game.SpaceInvadersInstanceMan;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useImmersiveMode = true;
		initialize(new SpaceInvadersInstanceMan(), config);
	}
}
