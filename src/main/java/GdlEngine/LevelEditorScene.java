package GdlEngine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){

    }

    @Override
    public void update(float dt) {
////       Check FPS
//        System.out.println("" + (1.0f/dt) + " Fps");
//
////       Allow scene change
//        if (!changingScene && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
//            changingScene = true;
//        }
//
////      Change scene with 2s transition
//        if(changingScene && timeToChangeScene > 0) {
//            timeToChangeScene -= dt;
//            Window.get().r -= dt * 5.0f;
//            Window.get().g -= dt * 5.0f;
//
//        } else if (changingScene) {
//            Window.changeScene(1);
//        }
    }
}
