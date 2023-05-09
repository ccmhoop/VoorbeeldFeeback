import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    ArrayList<BufferedImage> facingRightAnimation = new ArrayList<>();
    ArrayList<BufferedImage> facingLeftAnimation = new ArrayList<>();
    public static ArrayList<Integer> tileYAxisCollision = new ArrayList<>();
    String playerAction;
    public int playerPositionXAxis,playerPositionYAxis,playerSpeed=2;
    public int animationIndex;
    private final int defaultGravity = 5;
    private int timer,timeCycle,animationTimer;
    private int playerCollisionY,collisionPredictYAxis;
    public boolean jumpActivated,toggleGravity,faceRight;

    public void setAnimationArray(int pngLoadAmount, int directoryNumber) {
        for (int i = 1; i <= pngLoadAmount; i++) {
            try {
                facingLeftAnimation.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(animationPngPath(directoryNumber, i, "Left")))));
                facingRightAnimation.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(animationPngPath(directoryNumber, i, "Right")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String animationPngPath(int numberDirectory, int fileNumber, String directoryName) {
        return "/res/playerAnimation/" + numberDirectory + directoryName + "/" + directoryName + " " + "(" + fileNumber + ").png";
    }
    public int timeCycles(int timerSpeed, int timerCycles) {
        timer++;
        if (timer == timerSpeed) {
            timeCycle++;
        }
        if (timer > timerSpeed) {
            timer = 0;
        }
        if (timeCycle > timerCycles) {
            timeCycle = 0;
            timer = 0;
        }
        return timeCycle;
    }
    public void animationArrayIndexCycles(int speed, int indexStart, int indexEnd) {
        animationTimer++;
        if (animationTimer == speed) {
            animationIndex++;
        }
        if (animationIndex > indexEnd || animationIndex < indexStart) {
            animationIndex = indexStart;
        }
        if (animationTimer > speed) {
            animationTimer = 0;
        }
    }
    private void playerInput() {

        playerAction = KeyboardInputs.direction;

        if (keyBoard.leftPressed) {
            playerPositionXAxis -= playerSpeed;
            faceRight = false;
        } else if (keyBoard.rightPressed) {
            playerPositionXAxis += playerSpeed;
            faceRight = true;
        } else {
            playerAction = "idle";
        }
        if (keyBoard.spaceBar) {
            if (toggleGravity) {
                jumpActivated = true;
            }
        }
    }
    private void jump(int jumpSpeed,int jumpStages) {
        if (jumpActivated) {
            switch (timeCycles(jumpSpeed, jumpStages)) {
                //windup\\
                case 3, 4 -> playerPositionYAxis -= 5;
                case 5, 6 -> playerPositionYAxis -= 2;
                case 7, 8 -> playerPositionYAxis -= 1;
                case 9 -> jumpActivated = false;
            }
        }
    }
    private void playerAnimationDrawer(Graphics2D g2) {
        if (faceRight) {
            g2.drawImage(facingRightAnimation.get(animationIndex), playerPositionXAxis, playerPositionYAxis, 128, 128, null);
        } else {
            g2.drawImage(facingLeftAnimation.get(animationIndex), playerPositionXAxis, playerPositionYAxis, 128, 128, null);
        }
    }
    public int gravity(int entityPositionYAxis) {
        if (toggleGravity) {
            return entityPositionYAxis;
        } else {
            return entityPositionYAxis + defaultGravity;
        }
    }
    private void collisionPrediction() {
        for (int i = 10; 0 < i; i--) {
            collisionPredictYAxis = playerCollisionY - i;
            if (tileYAxisCollision.contains(collisionPredictYAxis)) {
                //collisionCheck(); <- deze methode vind ik niet netjes
            }
        }
    }
    private boolean setTileCollision(int tileNumber) {
        switch (tileNumber) {
            case 2, 6, 5 -> {
                return true;
            }
        }
        return false;
    }
}