package animations;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;

public class IconAnimation {

    private BufferedImage animationIcon;
    private final double DEFAULT_SCALE = 1.0;
    private final double POP_SCALE = 1.2;

    private double currentScale = 1.0;
    private double animationRate = 0.05;

    private boolean animaionCompleted = false;

    public IconAnimation() {
    }

    public boolean animationIsCompleted() {
        return this.animaionCompleted;
    }

    public BufferedImage getAnimation () {
        return animationIcon;
    }

    public BufferedImage popAnimation(BufferedImage img, int imgWidth, int imgHeight) {
        animationIcon = new BufferedImage(imgWidth * 2, imgHeight * 2, BufferedImage.TYPE_INT_ARGB);
        currentScale = POP_SCALE;

        if (!animaionCompleted) {
            AffineTransform transform = new AffineTransform();
            transform.translate(imgWidth / 2 - currentScale * imgWidth / 2, imgHeight / 2 - currentScale * imgHeight / 2);
            transform.scale(currentScale, currentScale);

            Graphics2D g2 = (Graphics2D) animationIcon.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            g2.drawImage(img, transform, null);
            currentScale -= animationRate;
            g2.dispose();

        }

        if (currentScale <= DEFAULT_SCALE) {
            animaionCompleted = true;
        }

        return animationIcon;

    }
}
