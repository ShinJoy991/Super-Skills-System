package com.github.shinjoy991.superskillssystem.gui.screen;

public class GuiSparkleParticle {
    public float x, y;
    public float vx, vy;
    public int lifetime;
    public int age;

    public GuiSparkleParticle(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = (float)(Math.random() - 0.5) * 0.5f;
        this.vy = (float)(Math.random() - 0.5) * 0.5f;
        this.lifetime = 40 + (int)(Math.random() * 20); // ~2s
        this.age = 0;
    }

    public void tick() {
        x += vx;
        y += vy;
        age++;
    }

    public boolean isAlive() {
        return age < lifetime;
    }

    public float getAlpha() {
        return 1.0f - (float) age / lifetime;
    }
}
