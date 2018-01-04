import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

/**
 * Created by terorie on 04.01.2018.
 */

// Simple image input: every pixel is an input
public class imageinput {

	private final int w, h;
	private final BufferedImage img;
	private final imgaccess access;

	public imageinput(BufferedImage img) {
		this.img = img;

		switch (img.getType()) {
			case BufferedImage.TYPE_BYTE_GRAY:
				this.access = new grayimg();
				break;
			case BufferedImage.TYPE_INT_RGB:
			case BufferedImage.TYPE_INT_ARGB:
				this.access = new argbimg();
				break;
			default:
				this.access = new javaimg();
				break;
		}

		this.w = img.getWidth();
		this.h = img.getHeight();
	}

	public float getInput(int i) {
		return access.getPixel(i);
	}

	public int getInputsCount() {
		return w * h;
	}


	private interface imgaccess {
		float getPixel(int i);
	}

	// Direct gray-scale access: Use gray-scale value (For pictures that only use a gray channel)
	private class grayimg implements imgaccess {
		private final byte[] pixels;

		grayimg() {
			pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		}

		@Override public float getPixel(int i) {
			// Values -128 -> 127
			int v = pixels[i];
			return (v+128f) / 255f;
		}
	}

	// Direct rgb access: Use image channel data directly (For RGB pictures (with or without alpha channel))
	private class argbimg implements imgaccess {
		private final int[] pixels;

		argbimg() {
			pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		}

		@Override public float getPixel(int i) {
			return rgbToGrey(pixels[i]);
		}
	}

	// Fallback method: Use getRGB to access pixels
	private class javaimg implements imgaccess {
		@Override public float getPixel(int i) {
			// X-Value: Column (Divide and only take rest)
			int x = i % w;

			// Y-Value: Row (Divide and discard rest)
			int y = i / w;

			// Get ARGB colors
			int argb = img.getRGB(x, y);

			return rgbToGrey(argb);
		}
	}

	// Converts an ARGB int (0xAARRGGBB) to a float from 0 -> 1
	private static float rgbToGrey(int argb) {
		// Red   channel (Bits 16-23)
		int r = (argb >> 16) & 0xFF;
		// Green channel (Bits 8-15)
		int g = (argb >> 8)  & 0xFF;
		// Blue  channel (Bits 0-7)
		int b =  argb        & 0xFF;

		// Calculate gray value
		int gray = r + g + b;

		return (float) gray / (3 * 255);
	}

}

