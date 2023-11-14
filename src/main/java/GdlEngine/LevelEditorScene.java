package GdlEngine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    //    Identifiers for what it's been used, (vertex, fragment or both) respectively
    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    private float[] vertexArray = {
//        position               // color                           //index
         100.5f, 0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,  // bottom right 0
        0.5f,  100.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,  // top left     1
         100.5f,  100.5f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,  // top right    2
        0.5f, 0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f   // bottom left  3
    };

    //    IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        0, 2, 1, // top right triangle
        1, 3, 0 // bottom left triangle

    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

//==================================================================
//        Generate VAO, VBO and EBO buffer objects and send to GPU
//        Buffer Objects - store array of unformatted memory allocated by the OpenGL context (GPU)
//        VAO - Vertical Array Objects - wrapper to store attributes in vertices, the VBO to use and elements.
//        VBO - Vertical Buffer Objects - contain vertex attributes and index (element) data.
//        EBO - Element Buffer Objects - define indices to use
//==================================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); //Orient the right way for openGL

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes; //total size of vertex in floats
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize,GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {

        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());

        // Bind VAO using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

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
