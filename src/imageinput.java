import java.awt.image.BufferedImage;

/**
 * Created by terorie on 04.01.2018.
 */

// Simple image input: every pixel is an input
public class imageinput {

	private final BufferedImage img;

	public imageinput(BufferedImage img) {
		this.img = img;
	}

	public float getInput(int i) {
		// Y: Line (Divide and ignore rest)
		int y = i / img.getWidth();
		// X: Column (Divide and only take rest)
		int x = i % img.getWidth();

		// Get ARGB colors
		int argb = img.getRGB(x, y);

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

	public int getInputsCount() {
		return img.getWidth() * img.getHeight();
	}

}

