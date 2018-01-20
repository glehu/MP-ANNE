package com.glehu.mpanne;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class randomImage
{
	private File[] dir;
	randomImage(File[] dir)
	{
		this.dir = dir;
	}

	private BufferedImage img;
	private int truth;

	void randImg()
	{
		img = null;
		Random rand = new Random();

		int dir_num = rand.nextInt(dir.length);
		File random_dir = dir[dir_num];
		File[] files = random_dir.listFiles();

		assert files != null;

		try
		{
			truth = dir_num;
			File image = files[rand.nextInt(files.length)];
			img = ImageIO.read(image);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(2);
		}
	}

	BufferedImage getRandImg()
	{
		return img;
	}

	int getTruth()
	{
		return truth;
	}
}
