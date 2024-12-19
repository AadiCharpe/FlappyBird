package org.example;

import processing.core.PApplet;
import processing.core.PImage;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MainApp extends PApplet {
    private int y = 250, score = 0;
    private boolean flap = false, dead = false, through = false, start = false;
    private PImage bird;
    private  double yV = 0;
    private ArrayList pipes;
    private Timer t;
    public static void main(String[] args) {
        PApplet.main("org.example.MainApp");
    }
    public void settings() {
        size(1200, 600);
    }
    public void setup() {
        pipes = new ArrayList<Pipe>();
        t = new Timer(1200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pipes.add(new Pipe());
            }
        });
        noStroke();
        bird = loadImage("src/sprites/bird.png");
    }
    public void draw() {
        if(!dead) {
            background(67, 78, 123);
            fill(50, 157, 67);
            for (int i = 0; i < pipes.size() - 1; i++) {
                Pipe p = (Pipe) pipes.get(i);
                p.move();
                rect(p.getX(), 0, 150, p.getY());
                rect(p.getX(), p.getY() + 200, 150, 400 - p.getY());
                if (p.getX() <= 200 && p.getX() + 150 >= 200) {
                    if((y < p.getY() || y > p.getY() + 200)) {
                        dead = true;
                        t.stop();
                    } else {
                        if(!through) {
                            score++;
                            through = true;
                        }
                    }
                }
                if(y <= 5 || y >= 600) {
                    dead = true;
                    t.stop();
                }
                if(p.getX() < -150) {
                    through = false;
                    pipes.remove(p);
                }
            }
            fill(255, 255, 255);
            textSize(48);
            text("Score: " + score, 24, 48);
            if(start) {
                yV += 0.5;
                y += yV;
            } else {
                textSize(64);
                text("Press Space to Start", 300, 400);
            }
            image(bird, 200, y, bird.width / 12, bird.height / 12);
        } else if(dead) {
            fill(245, 73, 26);
            textSize(56);
            text("Game Over", 425, 250);
            textSize(48);
            fill(255, 255, 255);
            text("Press Space to Restart", 350, 350);
        }
    }
    public void keyPressed() {
        if(key == ' ' && !flap) {
            yV = -9;
            flap = true;
            if(!start || dead) {
                score = 0;
                y = 250;
                start = true;
                pipes.clear();
                pipes.add(new Pipe());
                dead = false;
                t.start();
            }
        }
    }
    public void keyReleased() {
        flap = false;
    }
}

class Pipe {
    private int x = 1200, y;
    public Pipe() {y = (int) (Math.random() * 350) + 50;}
    public void move() {x -= 7;}
    public int getX() {return x;}
    public int getY() {return y;}
}