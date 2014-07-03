package package1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片压缩类
 * 
 * @author mou.shin
 * 
 */
@SuppressWarnings("restriction")
public class GraphicCompressUtil {

	public static final int NORMAL_QUALITY = 0;
	public static final int HIGH_QUALITY = 1;

	/**
	 * 压缩图片方法
	 * 
	 * @param oldFile
	 *            将要压缩的图片
	 * @param width
	 *            压缩宽
	 * @param quality
	 *            压缩清晰度 <b>建议为1.0</b>
	 * @param smallIcon
	 *            压缩图片后,添加的扩展名
	 * @return
	 */
	public String proce(String sourceFilePath, String sourceFolder, String targetFolder,
			int width, float quality,
			String smallIcon, int highQualityFlag) {
		
		if (sourceFilePath == null) {
			return null;
		}
		String newFileName = null;
		try {
			File file = new File(sourceFilePath);
			// 文件不存在时
			if (!file.exists()){
				return null;
			}
			/** 对服务器上的临时文件进行处理 */
			BufferedImage srcFile = ImageIO.read(file);
			int w = srcFile.getWidth(null); // 获得实际文件的宽度
			int h = srcFile.getHeight(null); // 获得实际文件的高

			/** 宽,高设定 */
			// 确定缩放宽和高，如果宽比较大，则把宽度缩放为输入值
			// 如果高度大于宽度，则确定高度为输入值
			if (w >= h) {
				float ww;
				ww = (float) w / (float) width;
				float hh = h / ww;
				h = (int) hh;
				w = width;
			} else {
				float ww;
				ww = (float) h / (float) width;
				float hh = w / ww;
				w = (int) hh;
				h = width;
			}

			// 用原图片的类型
			BufferedImage tag = new BufferedImage(w, h, srcFile.getType());
			if (highQualityFlag == HIGH_QUALITY) {
				// 图片压缩算法，使用getScaledInstance类实现，SCALE_SMOOTH为平滑过渡
				Image img2 = srcFile.getScaledInstance(w, h, Image.SCALE_SMOOTH);
				tag.getGraphics().drawImage(img2, 0, 0, w, h, null);
			} else {
				// 无平滑算法，直接缩放
				tag.getGraphics().drawImage(srcFile, 0, 0, w, h, null);
			}

			/** 压缩后的文件名 */
			String filePrex = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('.'));
			newFileName = filePrex + "_" + smallIcon + sourceFilePath.substring(filePrex.length());
			
			newFileName = newFileName.replace(sourceFolder, targetFolder);
			/** 压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(newFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			// 压缩质量
			// quality取值在 1.0 到 0.0 之间Some guidelines:
			// 0.75 high quality
			// 0.5 medium quality
			// 0.25 low quality
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);
			out.close();
			srcFile.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	//
	public static void main(String str[]) {
		GraphicCompressUtil ps = new GraphicCompressUtil();
		try {
			System.out.println(ps.proce("C:/DPP_0013.JPG","C:/", "C:/", 800, 1.0f, "high", HIGH_QUALITY));
			System.out.println(ps.proce("C:/DPP_0013.JPG","C:/", "C:/", 800, 0.5f, "low", NORMAL_QUALITY));
			System.out.print("成功哦");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}