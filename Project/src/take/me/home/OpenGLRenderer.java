package take.me.home;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.util.Random;
import android.opengl.GLSurfaceView.Renderer;

public class OpenGLRenderer implements Renderer{
	
	Random aleatorio = new Random();

	
	@Override
	
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1){
		
		float r = aleatorio.nextFloat();
		float g = aleatorio.nextFloat();
		float b = aleatorio.nextFloat();
		gl.glClearColor(r, g, b, 1.0f);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl){
		gl.glClear(gl.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height){
		
	}
	
}
