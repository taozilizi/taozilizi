package taozi.util;

import com.github.wycm.biz.constant.UrlConstant;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class OpenCvUtil {
    /**
     * 获取验证滑动距离
     * 
     * @return
     */
    //public static String dllPath = "D://opencv_java460.dll";

    public static int getDistance(String bUrl, String sUrl) {
        System.load(UrlConstant.opencvDll);
        /*File bFile = new File("C:/EasyDun_b.png");
        File sFile = new File("C:/EasyDun_s.png");*/
        File bFile = new File(bUrl);
        File sFile = new File(sUrl);
        try {
            /*FileUtils.copyURLToFile(new URL(bUrl), bFile);
            FileUtils.copyURLToFile(new URL(sUrl), sFile);*/
            BufferedImage bgBI = ImageIO.read(bFile);
            BufferedImage sBI = ImageIO.read(sFile);
            // 裁剪
            cropImage(bgBI, sBI, bFile, sFile);
            Mat s_mat = Imgcodecs.imread(sFile.getPath());
            Mat b_mat = Imgcodecs.imread(bFile.getPath());

            //阴影部分为黑底时需要转灰度和二值化，为白底时不需要
            // 转灰度图像
            Mat s_newMat = new Mat();
            Imgproc.cvtColor(s_mat, s_newMat, Imgproc.COLOR_BGR2GRAY);
            // 二值化图像
            binaryzation(s_newMat);
            Imgcodecs.imwrite(sFile.getPath(), s_newMat);

            int result_rows = b_mat.rows() - s_mat.rows() + 1;
            int result_cols = b_mat.cols() - s_mat.cols() + 1;
            Mat g_result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
            Imgproc.matchTemplate(b_mat, s_mat, g_result, Imgproc.TM_SQDIFF); // 归一化平方差匹配法TM_SQDIFF 相关系数匹配法TM_CCOEFF

            Core.normalize(g_result, g_result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
            Point matchLocation = new Point();
            Core.MinMaxLocResult mmlr = Core.minMaxLoc(g_result);
            matchLocation = mmlr.maxLoc; // 此处使用maxLoc还是minLoc取决于使用的匹配算法
            Imgproc.rectangle(b_mat, matchLocation, new Point(matchLocation.x + s_mat.cols(), matchLocation.y + s_mat.rows()), new Scalar(0, 255, 0, 0));
            Imgcodecs.imwrite(bFile.getPath(), b_mat);
            double dDis = matchLocation.x + s_mat.cols() - sBI.getWidth() + 12;
            dDis *= 1.25;//1.12是估计的网页图片比实际下载下来的图片大了1.12倍
            int distance = (int) dDis;//+10;//10是左边白边
            System.out.println("网页图计算出的距离为---------： " + distance);
            return distance;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        } finally {
            //bFile.delete();
           // sFile.delete();
        }
    }


    /**
     * 生成半透明小图并裁剪
     *
     * @param
     * @return
     */
    private static void cropImage(BufferedImage bigImage, BufferedImage smallImage, File bigFile, File smallFile) {
        int y = 0;
        int h_ = 0;
        try {
            // 2 生成半透明图片
            bloding(bigImage, 75);
            for (int w = 0; w < smallImage.getWidth(); w++) {
                for (int h = smallImage.getHeight() - 2; h >= 0; h--) {
                    int rgb = smallImage.getRGB(w, h);
                    int A = (rgb & 0xFF000000) >>> 24;
                    if (A >= 100) {
                        rgb = (127 << 24) | (rgb & 0x00ffffff);
                        smallImage.setRGB(w, h, rgb);
                    }
                }
            }
            for (int h = 1; h < smallImage.getHeight(); h++) {
                for (int w = 1; w < smallImage.getWidth(); w++) {
                    int rgb = smallImage.getRGB(w, h);
                    int A = (rgb & 0xFF000000) >>> 24;
                    if (A > 0) {
                        if (y == 0)
                            y = h;
                        h_ = h - y;
                        break;
                    }
                }
            }
            smallImage = smallImage.getSubimage(0, y, smallImage.getWidth(), h_);
            bigImage = bigImage.getSubimage(0, y, bigImage.getWidth(), h_);
            ImageIO.write(bigImage, "png", bigFile);
            ImageIO.write(smallImage, "png", smallFile);
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }

    /**
     *
     * @param mat
     *   二值化图像
     */
    public static void binaryzation(Mat mat) {
        int BLACK = 0;
        int WHITE = 255;
        int ucThre = 0, ucThre_new = 127;
        int nBack_count, nData_count;
        int nBack_sum, nData_sum;
        int nValue;
        int i, j;
        int width = mat.width(), height = mat.height();
        // 寻找最佳的阙值
        while (ucThre != ucThre_new) {
            nBack_sum = nData_sum = 0;
            nBack_count = nData_count = 0;

            for (j = 0; j < height; ++j) {
                for (i = 0; i < width; i++) {
                    nValue = (int) mat.get(j, i)[0];

                    if (nValue > ucThre_new) {
                        nBack_sum += nValue;
                        nBack_count++;
                    } else {
                        nData_sum += nValue;
                        nData_count++;
                    }
                }
            }
            nBack_sum = nBack_sum / nBack_count;
            nData_sum = nData_sum / nData_count;
            ucThre = ucThre_new;
            ucThre_new = (nBack_sum + nData_sum) / 2;
        }
        // 二值化处理
        int nBlack = 0;
        int nWhite = 0;
        for (j = 0; j < height; ++j) {
            for (i = 0; i < width; ++i) {
                nValue = (int) mat.get(j, i)[0];
                if (nValue > ucThre_new) {
                    mat.put(j, i, WHITE);
                    nWhite++;
                } else {
                    mat.put(j, i, BLACK);
                    nBlack++;
                }
            }
        }
        // 确保白底黑字
        if (nBlack > nWhite) {
            for (j = 0; j < height; ++j) {
                for (i = 0; i < width; ++i) {
                    nValue = (int) (mat.get(j, i)[0]);
                    if (nValue == 0) {
                        mat.put(j, i, WHITE);
                    } else {
                        mat.put(j, i, BLACK);
                    }
                }
            }
        }
    }


    /**
     * 图片亮度调整
     *
     * @param image
     * @param param
     * @throws IOException
     */
    public static void bloding(BufferedImage image, int param) throws IOException {
        if (image == null) {
            return;
        } else {
            int rgb, R, G, B;
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    rgb = image.getRGB(i, j);
                    R = ((rgb >> 16) & 0xff) - param;
                    G = ((rgb >> 8) & 0xff) - param;
                    B = (rgb & 0xff) - param;
                    rgb = ((clamp(255) & 0xff) << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8) | ((clamp(B) & 0xff));
                    image.setRGB(i, j, rgb);

                }
            }
        }
    }

    // 判断a,r,g,b值，大于256返回256，小于0则返回0,0到256之间则直接返回原始值
    private static int clamp(int rgb) {
        if (rgb > 255)
            return 255;
        if (rgb < 0)
            return 0;
        return rgb;
    }
}
