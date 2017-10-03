package com.mao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageParser {
	public static void main(String[] args) {
		try {
			BufferedImage image = ImageIO.read(new File("source.png"));
			for (int i = 0; i < Suit.values().length; i++) {
				for (int j = 0; j < Face.values().length; j++) {
					File file = new File("src/data/assets/images/" + Face.values()[j].name().toLowerCase() + "_of_"
							+ Suit.values()[i].name().toLowerCase() + ".png");
					file.delete();
					file.createNewFile();
					System.out.println(878 * j + 743 - image.getWidth());
					ImageIO.write(image.getSubimage(877 * j, 1040 * i, 744, 1040), "PNG", new FileOutputStream(file));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
