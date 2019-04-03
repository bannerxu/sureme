package top.banner.common.utils;

import com.alibaba.fastjson.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author jinguoguo
 */
public class PainterUtil {

    public final static Font CUSTOMER_NAME = new Font("宋体", Font.PLAIN, 36);
    public final static Color CUSTOMER_NAME_COLOR = new Color(0, 0, 0);
    public final static Font CUSTOMER_WECHAT_NUMBER = new Font("宋体", Font.PLAIN, 25);
    public final static Color CUSTOMER_WECHAT_NUMBER_COLOR = new Color(234, 53, 83);
    public final static Font CUSTOMER_PHONE_NUMBER = new Font("宋体", Font.PLAIN, 20);
    public final static Color CUSTOMER_PHONE_NUMBER_COLOR = new Color(50, 50, 50);


    /**
     * 绘制文字
     *
     * @param graphics2D 2D图形类
     * @param text       文本
     * @param font       字体
     * @param color      颜色
     * @param alpha      透明度
     * @param x          x轴坐标
     * @param y          y轴坐标
     */
    public static void paintText(Graphics2D graphics2D, String text, Font font, Color color, float alpha, int x, int y) {
        // 设置字体和颜色
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 在指定坐标绘制文字
        graphics2D.drawString(text, x, y);
    }


//    public static void paintImage(Image image, Graphics2D graphics2D, int x, int y, int width, int height, float alpha) {
//        Map map = calculateSize(image, x, y, width, height);
//        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
//        graphics2D.drawImage(image, (Integer) map.get("x"), (Integer) map.get("y"), (Integer) map.get("width"),
//                (Integer) map.get("height"), null);
//    }

    /**
     * 绘制网络图片
     *
     * @param graphics2D     Graphics2D对象
     * @param targetImageUrl 目标网络图片地址
     * @param x              x坐标
     * @param y              y坐标
     * @param width          图片指定宽度
     * @param height         图片指定高度
     */
    public static void paintRemoteImage(Graphics2D graphics2D, String targetImageUrl, int x, int y, int width, int height)
            throws IOException {
        URL url = new URL(targetImageUrl);

        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        InputStream in = connection.getInputStream();

        // 网络图片文件
        Image targetImage = ImageIO.read(connection.getInputStream());

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
        graphics2D.drawImage(targetImage, x, y, width, height, null);

        in.close();
    }

    public static BufferedImage createImage(String localFileUri) throws IOException {
        File file = new File(localFileUri);
        BufferedImage read = ImageIO.read(file);
        int width = read.getWidth(null);
        int height = read.getHeight(null);
        // 缓冲图片对象
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public static Graphics2D createGraphics2D(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).createGraphics();
    }

    public static void write(Graphics2D graphics2D, BufferedImage image, OutputStream outputStream) throws IOException {
        graphics2D.dispose();
        image.flush();
        ImageIO.write(image, "JPEG", outputStream);
    }

    public static void write(Graphics2D graphics2D, BufferedImage image, File file) throws IOException {
        graphics2D.dispose();
        image.flush();
        ImageIO.write(image, "JPEG", file);
    }

    // ==============================

    private static JSONObject getPressImageInfo(Image image, int x, int y, int mWidth, int mHeight) throws IllegalAccessException {
        JSONObject json = new JSONObject();
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (x >= mWidth || y >= mHeight) {
            json.put("x", 0);
            json.put("y", 0);
            json.put("w", 0);
            json.put("h", 0);
        } else if (x + w > mWidth || y + h > mHeight) {
            double _pw = ((double) mWidth - x) / w;
            double _ph = ((double) mHeight - y) / h;
            if (_pw >= _ph) {
                json.put("w", w * _ph);
                json.put("h", mHeight - y);
            } else {
                json.put("w", mWidth - x);
                json.put("h", h * _pw);
            }
            json.put("x", x);
            json.put("y", y);
        } else {
            json.put("x", x);
            json.put("y", y);
            json.put("w", w);
            json.put("h", h);
        }
        return json;
    }

    public static void paintImage(Image image, Graphics2D model, JSONObject json, float alpha) {
        model.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        model.drawImage(image, json.getInteger("x"), json.getInteger("y"), json.getInteger("w"), json.getInteger("h"), null);
    }

    public static void paintImage(Image image, Graphics2D model, int x, int y, int mWidth, int mHeight, float alpha) throws IllegalAccessException {
        JSONObject json = getPressImageInfo(image, x, y, mWidth, mHeight);
        paintImage(image, model, json, alpha);
    }
}
