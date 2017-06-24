package com.imageverify.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.imageverify.model.BufferedImageWrap;
import com.imageverify.model.GenerateImageGroup;
import com.imageverify.model.ImageGroup;

import sun.awt.SunHints;

public class ImageGenerate {
	private static final int WIDTH=100;
	private static final int HEADER=30;
	private static final int HEIGHT=100;	
	
	private static String realPath;
	private static Map<String,ImageGroup> imageGroups=new HashMap<>();
	private static Map<Integer,Map<String,ImageGroup>> countImageGroups=new HashMap<>();
	
	public static void init(){
		initImageGroup();
		initImageGroupMap();
	}
	
	public static void initImageGroup(){
		ImageGroup group=new ImageGroup("订书机",2,"dsj/1.jpg","dsj/2.jpg");
        ImageGroup group0=new ImageGroup("书本",2,"book/1.jpg","book/2.jpg");
        ImageGroup group1=new ImageGroup("蘑菇",2,"mg/1.jpg","mg/2.jpg");
        ImageGroup group2=new ImageGroup("云",2,"cloud/1.jpg","cloud/2.jpg");
        ImageGroup group3=new ImageGroup("吸铁石",2,"xts/1.jpg","xts/2.jpg");
        ImageGroup group4=new ImageGroup("包包",4,"bb/1.jpg","bb/2.jpg","bb/3.jpg","bb/4.jpg");
        ImageGroup group5=new ImageGroup("柚子",4,"yz/1.jpg","yz/2.jpg","yz/3.jpg","yz/4.jpg");
        ImageGroup group6=new ImageGroup("糖葫芦",4,"thl/1.jpg","thl/2.jpg","thl/3.jpg","thl/4.jpg");
        ImageGroup group7=new ImageGroup("老虎",4,"lh/1.jpg","lh/2.jpg","lh/3.jpg","lh/4.jpg");
        ImageGroup group8=new ImageGroup("土豆",6,"td/1.jpg","td/2.jpg","td/3.jpg","td/4.jpg","td/5.jpg","td/6.jpg");
        ImageGroup group9=new ImageGroup("仙人球",6,"xrq/1.jpg","xrq/2.jpg","xrq/3.jpg","xrq/4.jpg","xrq/5.jpg","xrq/6.jpg");
        ImageGroup group10=new ImageGroup("兔子",6,"tz/1.jpg","tz/2.jpg","tz/3.jpg","tz/4.jpg","tz/5.jpg","tz/6.jpg");
        initImageGroupMap(group,group0,group1,group2,group3,group4,group5,group6,group7,group8,group9,group10);
	}
	public static void initImageGroupMap(ImageGroup...groups){
		for(ImageGroup group:groups){
			imageGroups.put(group.getName(), group);
			if (!countImageGroups.containsKey(group.getCount())) {
				countImageGroups.put(group.getCount(), new HashMap<String,ImageGroup>());
			}
			countImageGroups.get(group.getCount()).put(group.getName(), group);
		}
	}
	public static void Genreate(HttpServletResponse response,HttpSession session){
		init();
		realPath=session.getServletContext().getRealPath("/assets/");
		GenerateImageGroup groups=getRandomImageGroup();
		List<BufferedImageWrap> images= getBufferedImageGroups(groups);
		
		mergeAndReturn(images,response,session);
	}
	//合并图片并返回至页面
	public static void mergeAndReturn(List<BufferedImageWrap> images,
			HttpServletResponse response,HttpSession session){
		Collections.shuffle(images);//打乱原有顺序
		BufferedImage targetImage=new BufferedImage(
				4*WIDTH,HEADER+2*HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		int x=0;
		int y=0;
		int order=0;
		List<Integer> keyOrders=new ArrayList<>();
		BufferedImage bufferedImage;
		boolean firstKey=true;
		for(BufferedImageWrap image:images){
			bufferedImage=image.getBufferedImage();
			
			int x1=0,y1=0;
			if(bufferedImage.getWidth()>WIDTH){
				x1=RandomIntGenerate.getRandomInt(bufferedImage.getWidth()-WIDTH);
			}
			if(bufferedImage.getHeight()>HEIGHT){
				y1=RandomIntGenerate.getRandomInt(bufferedImage.getHeight()-HEIGHT);
			}
			
			int[] rgb=bufferedImage.getRGB(x1,y1,WIDTH, HEIGHT,null, 0, WIDTH);
			if(image.isKey()){
				if(firstKey){
					Graphics2D g=(Graphics2D)targetImage.getGraphics();
					
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, 4*WIDTH, HEADER);
					
					g.setFont(new Font("宋体",Font.PLAIN,14));
					g.setColor(Color.BLACK);
					g.drawString("请在图下选中所有的  ", 0, HEADER/2);
					g.setFont(new Font("宋体",Font.BOLD,16));
					g.setColor(Color.RED);
					g.drawString(image.getName(), 150, HEADER/2);
					firstKey=false;
				}

				keyOrders.add(order);
				
			}
			x=(order%4)*WIDTH;
			y=order<4?HEADER:HEADER+HEIGHT;
			//System.out.println(x+"\n"+y);
			targetImage.setRGB(x, y, WIDTH, HEIGHT, rgb, 0, WIDTH);
			++order;
		}

		session.setAttribute("verifyCode", keyOrders);
		try {
			ImageIO.write(targetImage, "jpg", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List<BufferedImageWrap> getBufferedImageGroups(GenerateImageGroup generatedGroups){
		List<BufferedImageWrap> imageWraps=new ArrayList<>();
		for(String shortPath:generatedGroups.getKeyGroup().getPaths()){
			imageWraps.add(new BufferedImageWrap(true,generatedGroups.getKeyGroup().getName(),realPath+shortPath));
		}
		for(ImageGroup group:generatedGroups.getUnkeyGroups()){
			for(String shortPath:group.getPaths()){
				imageWraps.add(new BufferedImageWrap(false,"",realPath+shortPath));
			}
		}
		return imageWraps;
	}
	public static GenerateImageGroup getRandomImageGroup(){
		int randInt;
		String randName;
		int leftCount;
		ImageGroup keyImageGroup;
		List<ImageGroup> unKeyImageGroups=new ArrayList<>();
		
		//获取关键图片组
		randInt=RandomIntGenerate.getRandomInt(imageGroups.size());
		randName=new ArrayList<String>(imageGroups.keySet()).get(randInt);
		keyImageGroup=imageGroups.get(randName);
		
		//移除已选关键图片组
		Map<Integer,Map<String,ImageGroup>> tmpMap=new HashMap(countImageGroups);
		tmpMap.get(keyImageGroup.getCount()).remove(keyImageGroup.getName());
		
		leftCount=8-keyImageGroup.getCount();
		
		List<ImageGroup> groups;
		if(leftCount==6){
			//为偶数时，得到4+2图片组
			if(RandomIntGenerate.getRandomInt()%2==0){
				groups=new ArrayList<ImageGroup>(tmpMap.get(4).values());
				randInt=RandomIntGenerate.getRandomInt(groups.size());
				unKeyImageGroups.add(groups.get(randInt));
				
				groups=new ArrayList<ImageGroup>(tmpMap.get(2).values());
				randInt=RandomIntGenerate.getRandomInt(groups.size());
				unKeyImageGroups.add(groups.get(randInt));
			}
			//奇数时，得到3*2张图片组
			else{
				groups=new ArrayList<ImageGroup>(tmpMap.get(2).values());
				for(int i=0;i<3;i++){
					randInt=RandomIntGenerate.getRandomInt(groups.size());
					unKeyImageGroups.add(groups.get(randInt));
					groups.remove(randInt);
				}
			}
		}
		else if(leftCount==4){
			//得到1*4张图片组
			if(RandomIntGenerate.getRandomInt()%2==0){
				groups=new ArrayList<ImageGroup>(tmpMap.get(4).values());
				randInt=RandomIntGenerate.getRandomInt(groups.size());
				unKeyImageGroups.add(groups.get(randInt));
			}
			//得到2*2张图片组
			else{
				groups=new ArrayList<ImageGroup>(tmpMap.get(2).values());
				for(int i=0;i<2;i++){
					randInt=RandomIntGenerate.getRandomInt(groups.size());
					unKeyImageGroups.add(groups.get(randInt));
					groups.remove(randInt);
				}
			}
		}
		else if(leftCount==2){
			groups=new ArrayList<ImageGroup>(tmpMap.get(2).values());
			randInt=RandomIntGenerate.getRandomInt(groups.size());
			unKeyImageGroups.add(groups.get(randInt));
		}
		
		return new GenerateImageGroup(keyImageGroup,unKeyImageGroups);
	}
}
