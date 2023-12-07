//package com.libGdx.test.prv;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.kw.gdx.asset.Asset;
//import com.libGdx.test.base.LibGdxTestMain;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
///**
// * @Auther jian xian si qi
// * @Date 2023/12/7 16:13
// */
//public class PrvTest extends LibGdxTestMain {
//    public static void main(String[] args) {
//        PrvTest test = new PrvTest();
//        test.start();
//    }
//
//    @Override
//    public void useShow(Stage stage) {
//        super.useShow(stage);
////        Texture p656 = Asset.getAsset().getTexture("p656");
////        Image image = new Image(p656);
////        addActor(image);
//        FileHandle internal = Gdx.files.internal("p656");
//        // 指定PVR图片所在的文件夹
//        String inputFolder = "/path/to/pvr/images";
//
//        // 指定PNG输出文件夹
//        String outputFolder = "/path/to/png/output";
//
//        // 获取文件夹中所有PVR图片文件
//        File inputDir = new File(inputFolder);
//        File[] pvrFiles = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pvr"));
//
//        if (pvrFiles != null) {
//            // 遍历每个PVR文件并进行转换
//            for (File pvrFile : pvrFiles) {
//                convertPVRtoPNG(pvrFile, outputFolder);
//            }
//        } else {
//            System.out.println("No PVR files found in the specified folder.");
//        }
//
//    }
//
//        private static void convertPVRtoPNG(File pvrFile, String outputFolder) {
//            try {
//                // 解析PVR文件获取图像数据
//                BufferedImage image = readPVRFile(pvrFile);
//
//                // 构建PNG文件路径
//                String pngFileName = pvrFile.getName().replace(".pvr", ".png");
//                String pngFilePath = outputFolder + File.separator + pngFileName;
//
//                // 保存为PNG
//                ImageIO.write(image, "PNG", new File(pngFilePath));
//
//                System.out.println("Conversion successful: " + pvrFile.getName() + " -> " + pngFileName);
//            } catch (IOException e) {
//                System.err.println("Error converting " + pvrFile.getName() + ": " + e.getMessage());
//            }
//        }
//
//        private static BufferedImage readPVRFile(File pvrFile) throws IOException {
//            try (FileInputStream fis = new FileInputStream(pvrFile);
//                 XZInputStream xzis = new XZInputStream(fis, LZMA2Options.PRESET_DEFAULT);
//                 InputStreamReader reader = new InputStreamReader(xzis)) {
//
//                // 使用PVRTexLib来解析PVR文件
//                JsonParser jsonParser = new JsonParser();
//                JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);
//                JsonArray mipmaps = jsonObject.getArray("mipmaps");
//                JsonObject firstMipmap = mipmaps.getObject(0);
//
//                // 获取图像的宽度、高度和数据
//                int width = firstMipmap.getInt("width");
//                int height = firstMipmap.getInt("height");
//                String data = firstMipmap.getString("data");
//
//                // 将十六进制字符串解码为字节数组
//                byte[] imageData = javax.xml.bind.DatatypeConverter.parseHexBinary(data);
//                // 创建BufferedImage
//                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//                image.getRaster().setDataElements(0, 0, width, height, imageData);
//
//                return image;
//            }
//        }
//
//}
