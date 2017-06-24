package com.imageverify.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageWrap {
	private boolean key;//ÊÇ·ñ¹Ø¼üÍ¼Æ¬
	private String name;
	private BufferedImage bufferedImage;
	public BufferedImageWrap(boolean key,String name,String path){
		try {
			this.key=key;
			this.name=name;
			this.bufferedImage=ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
}
