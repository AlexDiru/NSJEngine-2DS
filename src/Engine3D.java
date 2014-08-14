import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * See: http://blog.xoppa.com/basic-3d-using-libgdx-2/
 * @author Xoppa
 */
public class Engine3D implements ApplicationListener {
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;


    public ArrayList<ModelInstance> instances;


    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 30000f;
        cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();

        WADReader.Wad wad = null;

        try {
            wad = WADReader.create("C:/Users/Alex/Dropbox/gzdoom-bin-1-8-02/doom.wad");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<WADReader.Wall> walls = wad.GetMaps().get(0).Walls;

        instances = new ArrayList<ModelInstance>();

        for (WADReader.Wall wall : walls) {
            float dX = wall.End.X - wall.Start.X;
            float dY = wall.End.Y - wall.Start.Y;
            float wallLength = (float)Math.sqrt(Math.pow(dX,2) + Math.pow(dY,2));
            float wallAngleRad = (float)Math.atan2(dY,dX) * (float)(180f/Math.PI);

            Model model = modelBuilder.createBox(wallLength, 50f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);

            ModelInstance ins = new ModelInstance(model, wall.Start.X,0,wall.End.X);
            ins.transform.rotate(0,1,0,wallAngleRad);

            instances.add(ins);
        }


        cam.position.set(0, 2000f,0);
        cam.lookAt(0, 0f,0);
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void render() {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        for (ModelInstance instance : instances)
            modelBatch.render(instance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}