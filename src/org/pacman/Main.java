package org.pacman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
//	private DeploymentGLView deplymentGLView;
//	private GameplayGLView gameplayGLView;
	/*
	 * 
	 */
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
    		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

//        DeploymentGLView currentGLView = new DeploymentGLView(this);
        GameplayGLView currentGLView = new GameplayGLView(this);
        setContentView(currentGLView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
//        deplymentGLView.onPause();
    }   
    
    @Override
    protected void onResume() {
        super.onResume();
//        deplymentGLView.onResume();
    }
}