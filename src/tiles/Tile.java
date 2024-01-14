package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Stroke;

import main.GamePanel;

public class Tile {

    public static final int TILE_WIDTH = 80;
    public static final int TILE_HEIGHT = 80;
    public static final int DEFAULT_TILE_SPEED = 20; // demonstration
    public static int TILE_SPEED = DEFAULT_TILE_SPEED;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;

    protected int number;
    protected BufferedImage tileImage;
    protected Color bgColor;
    protected Color txtColor;
    protected Font font;
    protected int x;
    protected int y;

    private Gameboard gameboard;

    private Destination pos;
    private boolean combinable = true;

    private boolean isSpawningAnimation = true;
    private double scaleOnSpawn = 0.1;
    private BufferedImage spawnImage;

    private boolean isVanishingAnimation = false;
    private double scaleOnVanish = 1;
    private BufferedImage vanishImage;

    private boolean isCombiningAnimation = false;
    private final double constCombiningScale = 1.2;
    private double combineScale = constCombiningScale;
    private BufferedImage combineImage;

    public Tile (int number, int row, int col, int drawX, int drawY, Gameboard gb) {
        this.number = number;
        this.x = drawX;
        this.y = drawY;
        pos = new Destination(row, col);

        this.gameboard = gb;

        spawnImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        vanishImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        combineImage = new BufferedImage((int)(TILE_WIDTH * constCombiningScale), (int)(TILE_HEIGHT * constCombiningScale), BufferedImage.TYPE_INT_ARGB);
        
        tileImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        updateImage();

    }

    public boolean isCombinable () {
        return combinable;
    }

    public void setCombinable (boolean b) {
        this.combinable = b;
    }

    public Destination getNewPos () {
        return this.pos;
    }

    public void setNewPos (Destination d) {
        this.pos = d;
    }

    public int getValue() {
        return number;
    }

    public void setValue(int num) {
        this.number = num;
        updateImage();
    }

    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public void setCombiningAnimation (boolean b) {
        isCombiningAnimation = b;
        combineScale = constCombiningScale;
    }

    public void setVanishingAnimation (boolean b) {
        isVanishingAnimation = b;
    }

    public boolean isVanishingAnimation () {
        return isVanishingAnimation;
    }

    public double getCurrentVanishScale () {
        return scaleOnVanish;
    }

    public boolean isVanished() {
        return scaleOnVanish <= 0.1;
    }

    public void setCurrentVanishScale (double v) {
        scaleOnVanish = v;
    }

    // graphics handling
    public void updateImage() {
        
        if (number == 2) {
            bgColor = new Color(0xe9e9e9);
            txtColor = new Color(0x0c0c0c);
        }
        
        else if (number == 4) {
            bgColor = new Color(0xa7cfdb);
            txtColor = new Color(0x0c0c0c);
        }
        
        else if (number == 8) {
            bgColor = new Color(0xf79d3d);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 16) {
            bgColor = new Color(0xf28007);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 32) {
            bgColor = new Color(0xf55e3b);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 64) {
            bgColor = new Color(0xff0000);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 128) {
            bgColor = new Color(0xe9de84);
            txtColor = new Color(0x0c0c0c);
        }
        
        else if (number == 256) {
            bgColor = new Color(0xf6e873);
            txtColor = new Color(0x0c0c0c);
        }
        
        else if (number == 512) {
            bgColor = new Color(0xf5e455);
            txtColor = new Color(0x0c0c0c);
        }
        
        else if (number == 1024) {
            bgColor = new Color(0x8c8429);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 2048) {
            bgColor = new Color(0x6b6058);
            txtColor = new Color(0xffffff);
        }
        
        else {
            bgColor = Color.BLACK;
            txtColor = Color.WHITE;
        }

        GradientPaint gradient = null;
        if (gameboard.attackIsReadyCheck()) {
            if (Gameboard.checkAttackingTile(pos.getRow(), pos.getCol())) {
                // drawing gradient cell
                Color startColor = Color.blue;
                Color endColor = Color.red;

                int startX = 10;
                int endX = 40;
                int startY = 60;
                int endY = 100;

                gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
                txtColor = Color.WHITE;
            }

            else if (Gameboard.checkHealingTile(pos.getRow(), pos.getCol())) {
                // drawing gradient cell
                Color startColor = Color.yellow;
                Color endColor = Color.green;

                int startX = 10;
                int endX = 40;
                int startY = 60;
                int endY = 100;

                gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
                txtColor = Color.BLACK;
            }
        }
        
        drawOnTileImage(gradient);
    }

    // decide the animation for the tile
    public void updateAnimation() {
        if (isSpawningAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(TILE_WIDTH / 2 - scaleOnSpawn * TILE_WIDTH / 2, TILE_HEIGHT / 2 - scaleOnSpawn * TILE_HEIGHT / 2);
            transform.scale(scaleOnSpawn, scaleOnSpawn);

            Graphics2D g2d = (Graphics2D) spawnImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            g2d.drawImage(tileImage, transform, null);
            scaleOnSpawn += 0.1;
            g2d.dispose();

            if (scaleOnSpawn >= 1) {
                isSpawningAnimation = false;
            }
        }

        else if (isVanishingAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(TILE_WIDTH / 2 - scaleOnVanish * TILE_WIDTH / 2, TILE_HEIGHT / 2 - scaleOnVanish * TILE_HEIGHT / 2);
            transform.scale(scaleOnVanish, scaleOnVanish);

            Graphics2D g2d = (Graphics2D) vanishImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            g2d.drawImage(tileImage, transform, null);
            scaleOnVanish -= 0.05;
            g2d.dispose();

            if (scaleOnVanish <= 0.1) {
                isVanishingAnimation = false;
            }
        }

        else if (isCombiningAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.scale(combineScale, combineScale);

            Graphics2D g2d = (Graphics2D) combineImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            g2d.drawImage(tileImage, transform, null);

            combineScale -= 0.05;
            g2d.dispose();

            if (combineScale <= 1) {
                isCombiningAnimation = false;
            }
        }
    }

    public void drawOnTileImage(GradientPaint gradientPaint) {
        Graphics2D g2 = (Graphics2D) tileImage.getGraphics();
        
        if (gradientPaint != null)
            g2.setPaint(gradientPaint);
        else
            g2.setColor(bgColor);

        g2.fillRoundRect(0, 0, TILE_WIDTH, TILE_HEIGHT, ARC_WIDTH, ARC_HEIGHT);
        Stroke stroke = new BasicStroke(8f);
        g2.setColor(new Color(0x858267));
        g2.setStroke(stroke);
        g2.drawRoundRect(0, 0, TILE_WIDTH, TILE_HEIGHT, ARC_WIDTH * 2, ARC_HEIGHT * 2);
        g2.setColor(txtColor);
        
        if (number <= 64) {
            font = GamePanel.font.deriveFont(36f);
        }
        else {
            font = GamePanel.font;
            g2.setFont(font);
        }

        g2.setFont(font);

        int drawX = TILE_WIDTH / 2 - DrawUtilz.getMessageWidth("" + number, font, g2) / 2;
        /* Half the tile, and half the text width */
        int drawY = TILE_HEIGHT / 2 + DrawUtilz.getMessageHeight("" + number, font, g2) / 2;
        
        g2.drawString("" + number, drawX, drawY);
        g2.dispose();
    }

    // render the chosen animation from the update() method
    public void drawTile(Graphics2D g) {

        if (isSpawningAnimation) {
            g.drawImage(spawnImage, x, y, null); 
        }

        else if (isVanishingAnimation) {
            g.drawImage(vanishImage, x, y, null); 
        }

        else if (isCombiningAnimation) {
            g.drawImage(combineImage, (int)(x - (TILE_WIDTH * combineScale - TILE_WIDTH) / 2),
                                        (int) (y - (TILE_HEIGHT * combineScale - TILE_HEIGHT) / 2), null);
        }

        else {
            g.drawImage(tileImage, x, y, null);
        }
    }
}