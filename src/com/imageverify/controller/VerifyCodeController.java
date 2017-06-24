package com.imageverify.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imageverify.util.ImageGenerate;

@Controller
public class VerifyCodeController {
	@RequestMapping("/getVerifyCode")
	public void getVerifyCode(HttpServletResponse response,HttpSession session){
		ImageGenerate.Genreate(response, session);
	}
	@RequestMapping("/verify")
	@ResponseBody
	public String verify(String verifyCode,HttpSession session){
		String [] coordinates=verifyCode.split(";");
		List<Integer> orders=(List<Integer>)session.getAttribute("verifyCode");
		if(coordinates.length!=orders.size()){
			return "unpass";
		}
		
		int order=0;
		for(String coordinate:coordinates){
			String[] offsets=coordinate.split(",");
			order=getSelectedOrder(Integer.parseInt(offsets[0]),Integer.parseInt(offsets[1]));
			//System.out.println(order);
			if(!orders.contains(order)){
				return "unpass";
			}
		}
		//System.out.println(verifyCode);
		return "pass";
	}
	
	public int getSelectedOrder(int x,int y){
		int order;
		if(x>300){
			order=3;
		}
		else if(x>200){
			order=2;
		}
		else if(x>100){
			order=1;
		}
		else{
			order=0;
		}
		
		if(y>100){
			order+=4;
		}
		return order;
	}
}
