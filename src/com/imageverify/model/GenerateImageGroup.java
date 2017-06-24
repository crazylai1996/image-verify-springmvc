package com.imageverify.model;

import java.util.List;

//生成的图片组
public class GenerateImageGroup {
	private ImageGroup keyGroup;//关键图片组
	private List<ImageGroup> unkeyGroups;//其他图片组
	
	public GenerateImageGroup(ImageGroup keyGroup,List<ImageGroup> unkeyGroups){
		this.keyGroup=keyGroup;
		this.unkeyGroups=unkeyGroups;
	}
	
	public ImageGroup getKeyGroup() {
		return keyGroup;
	}
	public void setKeyGroup(ImageGroup keyGroup) {
		this.keyGroup = keyGroup;
	}
	public List<ImageGroup> getUnkeyGroups() {
		return unkeyGroups;
	}
	public void setUnkeyGroups(List<ImageGroup> unkeyGroups) {
		this.unkeyGroups = unkeyGroups;
	}
}
