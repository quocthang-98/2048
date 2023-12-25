package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Stroke;

import main.GamePanel;

public class Tile {

    public static final int TILE_WIDTH = 80;
    public static final int TILE_HEIGHT = 80;
    public static final int TILE_SPEED = 20;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;

    protected int number;
    protected BufferedImage tileImage;
    protected Color bgColor;
    protected Color txtColor;
    protected Font font;
    protected int x;
    protected int y;

    private Destination newPos;
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

    public Tile (int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
        newPos = new Destination(x, y);

        spawnImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        vanishImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        combineImage = new BufferedImage((int)(TILE_WIDTH * constCombiningScale), (int)(TILE_HEIGHT * constCombiningScale), BufferedImage.TYPE_INT_ARGB);
        
        tileImage = new BufferedImage(TILE_WIDTH, TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        drawImage();
    }

    public boolean isCombinable () {
        return combinable;
    }

    public void setCombinable (boolean b) {
        this.combinable = b;
    }

    public Destination getNewPos () {
        return this.newPos;
    }

    public void setNewPos (Destination d) {
        this.newPos = d;
    }

    public int getValue() {
        return number;
    }

    public void setValue(int num) {
        this.number = num;
        drawImage();
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
        scaleOnVanish = 1;
    }

    public boolean isVanishingAnimation () {
        return isVanishingAnimation;
    }

    // graphics handling
    public void drawImage() {
        Graphics2D g = (Graphics2D) tileImage.getGraphics();

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
            bgColor = new Color(0xf7e12c);
            txtColor = new Color(0xffffff);
        }
        
        else if (number == 2048) {
            bgColor = new Color(0xffe400);
            txtColor = new Color(0xffffff);
        }
        
        else {
            bgColor = Color.BLACK;
            txtColor = Color.WHITE;
        }

        g.setColor(bgColor);
        g.fillRoundRect(0, 0, TILE_WIDTH, TILE_HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        Stroke stroke = new BasicStroke(8f);
        g.setColor(new Color(0x858267));
        g.setStroke(stroke);
        g.drawRoundRect(0, 0, TILE_WIDTH, TILE_HEIGHT, ARC_WIDTH * 2, ARC_HEIGHT * 2);
        g.setColor(txtColor);
        
        if (number <= 64) {
            font = GamePanel.font.deriveFont(36f);
        }
        else {
            font = GamePanel.font;
            g.setFont(font);
        }

        g.setFont(font);

        int drawX = TILE_WIDTH / 2 - DrawUtilz.getMessageWidth("" + number, font, g) / 2;
        /* Half the tile, and half the text width */
        int drawY = TILE_HEIGHT / 2 + DrawUtilz.getMessageHeight("" + number, font, g) / 2;
        
        g.drawString("" + number, drawX, drawY);
        g.dispose();
    }

    // decide the animation for the tile
    public void update() {
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

            Graphics2D g2d = (Graphics2D) spawnImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            
            g2d.drawImage(tileImage, transform, null);
            scaleOnSpawn -= 0.05;
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