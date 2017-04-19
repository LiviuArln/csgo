package zzz;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class Mouse  {
	public static class MoveListener implements NativeKeyListener{
		private Set<String> directions = new HashSet<String>();
		private List<String> keys = new ArrayList<String>() {{
			add("A");add("D");add("W");add("S");
		}};
		
		@Override
		public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
		}

		@Override
		public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
			String keyText = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
			if(keys.contains(keyText)) {
				directions.add(keyText);
			}
		}

		@Override
		public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
			String keyText = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
			if(keys.contains(keyText)) {
				directions.remove(keyText);
			}
		}
		
		public boolean isMoving() {
			//System.err.println(directions.size());
			return directions.size() != 0;
		}	
	}
	
	public static class ShootListener implements NativeMouseListener {
		private MoveListener ml;
		
		public ShootListener(MoveListener ml) {
			this.ml = ml;
		}
		
		@Override
		public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
		}

		@Override
		public void nativeMousePressed(NativeMouseEvent nativeEvent) {
			if(nativeEvent.getButton() == 1 && ml.isMoving()) {
				Toolkit.getDefaultToolkit().beep();
			}
		}

		@Override
		public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
		}
		
	}

    public static void main(String[] args) {
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    	logger.setLevel(Level.OFF);
    	
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            System.exit(1);
        }

        MoveListener moveListener = new MoveListener();
		GlobalScreen.addNativeKeyListener(moveListener );
        GlobalScreen.addNativeMouseListener(new ShootListener(moveListener));
    }

}
