package com.danish.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import org.omg.PortableInterceptor.Interceptor;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	int numberOfTubes = 4;
	Texture background, birds[], topPipes[],bottomPipes[];

	int flapState;
	int birdY, downSpeed;
	int gravity, gap = 500;
	int tubeoffset[];
	Random randomgenerator = new Random();
	int tubeVelocity;
	int tubeX[];
	int disctanceBetweenubes;
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	Rectangle tubeRectTop[];
	Rectangle tubeRectBottom[];
	int gameState = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		tubeoffset = new int[4];
		tubeX = new int[numberOfTubes];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topPipes = new Texture[numberOfTubes];
		bottomPipes = new Texture[numberOfTubes];
		birdCircle = new Circle();
		tubeRectTop = new Rectangle[numberOfTubes];
		tubeRectBottom = new Rectangle[numberOfTubes];
		shapeRenderer = new ShapeRenderer();

		//topPipes = new Texture("toptube.png");
		//bottomPipes = new Texture("bottomtube.png");

		flapState = 0;
		birdY = Gdx.graphics.getHeight()/2-birds[flapState].getHeight()/2;
		downSpeed = 0;
		gravity = 1;
		tubeVelocity = 4;
		//tubeX =  Gdx.graphics.getWidth()/2-topPipes.getWidth()/2;
		numberOfTubes = 4;
		disctanceBetweenubes  = Gdx.graphics.getWidth()*3/4;

		for (int i = 0; i < numberOfTubes; i++){
			topPipes[i] = new Texture("toptube.png");
			bottomPipes[i] = new Texture("bottomtube.png");

			tubeoffset[i] = (int )((randomgenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap -400));
			tubeX[i]= 2*Gdx.graphics.getWidth()-topPipes[i].getWidth()/2 + i*disctanceBetweenubes;
			tubeRectBottom[i] = new Rectangle();
			tubeRectTop[i] = new Rectangle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		if(gameState == 2) {
			batch.end();
			return;
		}
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(Gdx.input.justTouched()){

			Gdx.app.log("Touched", "Its touched");
			downSpeed = -20;//(downSpeed + gravity*5)*-1;
			float random = randomgenerator.nextFloat();
			for(int i = 0; i < numberOfTubes; i++){
				//tubeoffset[i] = (int )((randomgenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap -400));
				//tubeX[i] =  Gdx.graphics.getWidth()/2-topPipes[i].getWidth()/2 + tubeoffset[i];

			}

		}

		for(int i = 0; i < numberOfTubes; i++) {
			tubeX[i] =tubeX[i] - tubeVelocity;
			if(tubeX[i] < 0-topPipes[i].getWidth() ) {
				tubeX[i] = numberOfTubes * disctanceBetweenubes;
				tubeoffset[i] = (int )((randomgenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap -200));
			}

			batch.draw(topPipes[i], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
			batch.draw(bottomPipes[i], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomPipes[i].getHeight() + tubeoffset[i]);
			tubeRectTop[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],topPipes[i].getWidth(),topPipes[i].getHeight());
			tubeRectBottom[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomPipes[i].getHeight() + tubeoffset[i],topPipes[i].getWidth(),topPipes[i].getHeight());
		}
		if(birdY > 0 || downSpeed < 0 ) {
			downSpeed = downSpeed + gravity;
			birdY = birdY - downSpeed;
		}
		if(flapState == 0)
			flapState = 1;
		else
			flapState = 0;


		batch.draw(birds[flapState], Gdx.graphics.getWidth()/2-birds[flapState].getWidth()/2, birdY);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[0].getWidth()/2, birds[0].getWidth()/2-30);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.FIREBRICK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y, birdCircle.radius);

		for(int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],topPipes[i].getWidth(),topPipes[i].getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomPipes[i].getHeight() + tubeoffset[i],topPipes[i].getWidth(),topPipes[i].getHeight());
			if(Intersector.overlaps(birdCircle,tubeRectBottom[i]) || Intersector.overlaps(birdCircle,tubeRectTop[i])){
				gameState = 2;

			}
		}

		//shapeRenderer.end();

		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
