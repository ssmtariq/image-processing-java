package com.ssmtariq;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RecolorFlowersSingleThread {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        recolorSingleThreaded(originalImage, resultImage);
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage,"jpg", outputFile);
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage){
        recolorImage(originalImage, resultImage, 0,0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height){
        for (int x=leftCorner; x<leftCorner+width && x<originalImage.getWidth(); x++){
            for (int y=topCorner; y<topCorner+height && y<originalImage.getHeight(); y++){
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y){
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if(isShadeOfGray(red, green, blue)){
            newRed = Math.min(255, red+10);
            newGreen = Math.max(0, green-80);
            newBlue = Math.max(0, blue-20);
        }else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createdRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb){
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue){
        return Math.abs(red-green) <30 && Math.abs(red-blue)<30 && Math.abs(green-blue)<30;
    }

    /**
     * The format of your input is 0xAARRGGBB where
     * AA is the alpha (transparency),
     * RR is the red,
     * GG is the green, and
     * BB is the blue component.
     * This is hexadecimal, so the values range from 00 to FF (255)
     *
     * @param red int
     * @param green int
     * @param blue int
     * @return rgb int
     */
    public static int createdRGBFromColors(int red, int green, int blue){
        int rgb =0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb){
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb){
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb){
        return rgb & 0x000000FF;
    }
}
