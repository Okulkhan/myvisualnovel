package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChoiceButton extends Image {

    private final Texture texture;
    private Runnable onClickListener;

    public ChoiceButton(Texture texture) {
        super(texture);
        this.texture = texture;
        setSize(texture.getWidth(), texture.getHeight());

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickListener != null) {
                    onClickListener.run();
                }
            }
        });
    }
    public void setOnClickListener(Runnable listener) {
        this.onClickListener = listener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }
}
