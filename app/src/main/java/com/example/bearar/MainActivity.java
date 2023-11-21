package com.example.bearar;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable renderable; // Declare the variable at the class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "model/bear.glb"; // Replace with the actual asset name

        RenderableSource renderableSource = RenderableSource.builder()
                .setSource(
                        this,
                        Uri.parse("asset://" + path), // Correct syntax for asset path
                        RenderableSource.SourceType.GLB
                )
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable.builder()
                .setSource(
                        this,
                        renderableSource // This is the model we built earlier
                )
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable; // Store in a variable to reference later
                })
                .exceptionally(error -> {
                    // Handle errors here
                    return null;
                });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.ux_fragment);

        if (fragment != null && fragment instanceof ArFragment) {
            ArFragment arFragment = (ArFragment) fragment;

            arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                transformableNode.setRenderable(renderable);
                transformableNode.setParent(anchorNode);
                transformableNode.select();
            });
        }
    }
}
