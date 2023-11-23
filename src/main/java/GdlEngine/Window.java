package GdlEngine;

import Util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;
    private static Scene currentScene;

    float r,g;
    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "GEngine";
        r = 1.0f;
        g = 1.0f;

    }

//    Selects a scene
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 -> {
                currentScene = new LevelEditorScene();
                currentScene.init();
            }
            case 1 -> {
                currentScene = new LevelScene();
                currentScene.init();
            }
            default -> {
                assert false : "Unknown Scene " + newScene;
            }
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();

//        Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

//        Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();


    }

    public void init() {
//        Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();
//        Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

//        Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //window stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // window resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

//      Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

//      Mouse position, button and scroll callback
        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
//        Keys callback
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);

//        Make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
//        Enable v-sync
        glfwSwapInterval(1);

//        Make window visible
        glfwShowWindow(glfwWindow);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
    }
    public void loop() {
//      time the frame begin and end
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
//            Pull events
            glfwPollEvents();

            glClearColor(r, g, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                //System.out.println("Space key was pressed");
            }

            if(dt >= 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
