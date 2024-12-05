package apprentissage;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This utility class contains unit functions used by the train simulation.
 * 
 * @see SimuPara
 * @author Tianxiao.Liu@u-cergy.fr
 */
public class Image {

    public static int SCALE_SMOOTH;

	/**
     * Reads an image from an image file.
     * 
     * @param filePath the path (from "src") of the image file
     * @return the read image
     */
    public static BufferedImage readImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("-- Can not read the image file ! --");
            return null;
        }
    }
}
