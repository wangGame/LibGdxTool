package kw.learn.libgdxrare.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ConvertImageSize {
    public static void main (String[] args) throws IOException {
        String path = "0_1_41_512.jpg";
        String path2 = "data/icon/";
        String name = "icon72.png";
        BufferedImage read = ImageIO.read(new File(path));
        String[] path3 = {"drawable-hdpi", "drawable-ldpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi",};
        int[] size = {72, 36, 48, 96, 144,};
        for (int i = 0; i < path3.length; i++) {
            int targetsize = size[i];
            Image smallone0 = read.getScaledInstance(targetsize, targetsize, BufferedImage.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(targetsize, targetsize, read.getType());
            bufferedImage.getGraphics().drawImage(smallone0, 0, 0, targetsize, targetsize, 0, 0, targetsize, targetsize, null);
            File output = new File(path2 + path3[i] + "/" + name);
            if (!output.getParentFile().exists())
                output.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, "png", output);
        }
    }
}
