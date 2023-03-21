package com.funny.combo.trade.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: funnystack
 * @create: 2019-06-05 14:08
 */
public class QRCodeEncode {
    private static final Map<EncodeHintType, Object> hints = new HashMap<>();
    private static final String CHARSET = "utf-8";//编码

    static {
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
    }

    private final String text;//内容
    private int size = 300;//尺寸

    private String format = "JPG";//格式

    private String logoFile = "";//logo文件路径
    private int logoWidth = 60;//logo宽度
    private int logoHeight = 60;//logo高度
    private boolean compress = false;//logo是否压缩（rescale）

    private String filePath;//输出文件路径
    private String fileName;//输出文件名

    private String bottomNote;

    public QRCodeEncode() {
        this.text = "";
    }

    public QRCodeEncode(String text) {
        this.text = text;
    }

    /**
     * @param text
     * @return
     */
    public static QRCodeEncode from(String text) {
        return new QRCodeEncode(text);
    }

    /**
     * 大小
     *
     * @param size
     * @return
     */
    public QRCodeEncode withSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * logo大小
     *
     * @param width
     * @param heigth
     * @return
     */
    public QRCodeEncode withLogoSize(int width, int heigth) {
        this.logoWidth = width;
        this.logoHeight = heigth;
        return this;
    }

    /**
     * logo图片地址
     *
     * @param logo
     * @return
     */
    public QRCodeEncode withLogo(String logo) {
        if (logo == null || "".equals(logo)) {
            return this;
        }
        this.logoFile = logo;
        return this;
    }

    /**
     * logo是否压缩
     *
     * @param compress
     * @return
     */
    public QRCodeEncode compress(boolean compress) {
        this.compress = compress;
        return this;
    }

    /**
     * 输出格式
     *
     * @param format
     * @return
     */
    public QRCodeEncode withFormat(String format) {
        if (format == null || "".equals(format)) {
            return this;
        }
        this.format = format;
        return this;
    }

    /**
     * 输出文件路径
     *
     * @param filePath
     * @return
     */
    public QRCodeEncode withFilePath(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return this;
        }
        this.filePath = filePath;
        return this;
    }

    /**
     * 输出文件名
     *
     * @param fileName
     * @return
     */
    public QRCodeEncode withFileName(String fileName) {
        if (fileName == null || "".equals(fileName)) {
            return this;
        }
        this.fileName = fileName;
        return this;
    }

    /**
     * 字符集
     *
     * @param charset
     * @return
     */
    public QRCodeEncode withCharset(String charset) {
        if (charset == null || "".equals(charset)) {
            charset = CHARSET;
        }
        return withHint(EncodeHintType.CHARACTER_SET, charset);
    }

    /**
     * 纠错级别
     *
     * @param level
     * @return
     */
    public QRCodeEncode withErrorCorrection(ErrorCorrectionLevel level) {
        return withHint(EncodeHintType.ERROR_CORRECTION, level);
    }

    /**
     * 页边空白
     *
     * @param margin
     * @return
     */
    public QRCodeEncode withMargin(int margin) {
        return withHint(EncodeHintType.MARGIN, margin);
    }

    /**
     * 配置hints
     *
     * @param hintType
     * @param value
     * @return
     */
    public QRCodeEncode withHint(EncodeHintType hintType, Object value) {
        hints.put(hintType, value);
        return this;
    }

    /**
     * 文字
     *
     * @param bottomNote
     * @return
     */
    public QRCodeEncode withBottomNote(String bottomNote) {
        this.bottomNote = bottomNote;
        return this;
    }

    /**
     * 输出文件，返回生成后的路径
     *
     * @return
     * @throws Exception
     */
    public String fileName() throws Exception {
        return toFile().getAbsolutePath();
    }

    /**
     * 输出文件，返回文件file
     *
     * @return
     * @throws Exception
     */
    public File toFile() throws Exception {
        BufferedImage image = createImage();
        mkdirs(filePath);
        fileName = this.fileName();
        if (fileName == null || fileName == "") {
            fileName = new Random().nextInt(99999999) + "." + format.toLowerCase();
        }
        File file = new File(filePath + "/" + fileName);
        ImageIO.write(image, format, file);
        return file;
    }

    /**
     * 写到字节流
     *
     * @param output
     * @throws Exception
     */
    public void toStream(OutputStream output) throws Exception {
        BufferedImage image = createImage();
        ImageIO.write(image, format, output);
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．
     * (mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 插入LOGO
     *
     * @param source   二维码图片
     * @param logoPath LOGO图片地址
     * @param compress 是否压缩
     * @throws Exception
     */
    private void addMiddleImage(BufferedImage source, String logoPath, boolean compress) throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            throw new RuntimeException("logoFile file not found.");
        }

        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (compress) { // 压缩LOGO
            if (width > this.logoWidth) {
                width = this.logoWidth;
            }
            if (height > this.logoHeight) {
                height = this.logoHeight;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (this.size - width) / 2;
        int y = (this.size - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 增加底部文字
     *
     * @param note
     * @return
     */
    private BufferedImage addBottomNote(BufferedImage srcImage, String note) {
        final int fontHeight = 20;
        final int fontMaxSize = 16;
        BufferedImage newImage;
        if (note.length() <= fontMaxSize) {
            newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight() + fontHeight, BufferedImage.TYPE_INT_RGB);
        } else {
            newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight() + fontHeight * 2, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D g = newImage.createGraphics();//设置文字

        try {
            Font font = new Font("宋体", Font.PLAIN, 18);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(font);
            g.setBackground(Color.WHITE);
            g.clearRect(0, srcImage.getHeight(), srcImage.getWidth(), fontHeight);
            g.setPaint(Color.BLACK);

            g.drawImage(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), null);

            if (note.length() <= fontMaxSize) {
                g.drawString(note, size / 2 - note.length() * 8 - 14, size + fontHeight / 2);
            } else {
                g.drawString(note.substring(0, fontMaxSize), size / 2 - fontMaxSize * 8 - 14, size + fontHeight / 2);
                g.clearRect(0, srcImage.getHeight() + fontHeight, srcImage.getWidth(), fontHeight);

                int length = note.length() >= fontMaxSize * 2 ? fontMaxSize : note.length() - 16;
                g.drawString(note.substring(fontMaxSize, fontMaxSize + length), size / 2 - length * 8 - 14, size + fontHeight + 8);
            }
        } finally {
            g.dispose();
            newImage.flush();
        }

        return newImage;
    }

    /**
     * 创建图片
     * @return
     * @throws Exception
     */
    private BufferedImage createImage() throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, this.size, this.size, this.hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 插入图片
        if (logoFile != null && !"".equals(logoFile)) {
            addMiddleImage(image, logoFile, compress);
        }

        //插入底部文字
        if (bottomNote != null && !"".equals(bottomNote)) {
            image = addBottomNote(image, bottomNote);
        }


        return image;
    }
}
