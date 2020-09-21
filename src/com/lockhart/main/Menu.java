package com.lockhart.main;

import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.lockhart.world.World;



public class Menu {
	private BufferedImage[] fundo;
	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	public int currentOption = 0, index = 0, maxIndex = 3, frames = 0, maxFrames = 40
			;
	public int maxOption = options.length-1;
	public boolean up=false, down=false, enter;
	public static boolean pause =false;
	public static boolean saveExists =false;
	public static boolean saveGame =false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption--;
				if(currentOption < 0) {
					currentOption = maxOption;
				}
		}if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState =  "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption] == "Carregar Jogo"){
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
				
			}
		}
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index >  maxIndex) 
				index = 0;
		}
		
		
		
		
	}
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String [] spl2 = spl[i].split(":");
			switch(spl2[0])
			{
			case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState =  "NORMAL";
				pause = false;
				break;
			case "vida":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			}
		}
	}
	
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
			try {
				while((singleLine = reader.readLine())!= null ){
					String[] trans = singleLine.split(":");
					char[] val = trans[1].toCharArray();
					trans[1] = "";
					for (int i = 0 ; i < val.length; i++) {
						val[i]-=encode;
						trans[1]+=val[i];
					}
					line+=trans[0];
					line+=":";
					line+=trans[1];
					line+="/";
				}
			}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		
		return line;
	}
	public static void saveGame(String[] val1, int[] val2, int encode ) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0 ; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if( i < val1.length - 1) {
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e){		
		}
	}
	public void render(Graphics g) {
	if(pause == false) {
		fundo = new BufferedImage[4];
		for(int i = 0; i<4; i++) {
			fundo[i] = Game.Newfundo.getSprite(0+(i*720), 0, 720, 480);
			
			}
		g.drawImage(fundo[index], 0 , 0 ,null);
		//System.out.println(index);
	}else{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color (0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
	}
	//op��es do jogo
		g.setColor(Color.black);
		g.setFont(Game.newfont);
		//g.setFont(new Font("arial", Font.BOLD, 24));
		if(pause == false) {
		g.drawString(" Novo Jogo ",(Game.WIDTH*Game.SCALE)/2 - 55, 200);
		g.drawString(" Carregar Jogo ",(Game.WIDTH*Game.SCALE)/2 - 75, 250);
		g.drawString(" Sair ",(Game.WIDTH*Game.SCALE)/2 - 15, 300 );
	     }else {
		g.drawString(" Continuar ",(Game.WIDTH*Game.SCALE)/2 - 50, 200);
		g.drawString(" Carregar Jogo ",(Game.WIDTH*Game.SCALE)/2 - 75, 250);
		g.drawString(" Sair ",(Game.WIDTH*Game.SCALE)/2 - 15, 300 );
		}if(options[currentOption]=="Novo Jogo") {
			g.setColor(Color.RED);
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 65, 200);
		}else if(options[currentOption]=="Carregar Jogo") {
			g.setColor(Color.RED);
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 85, 250);
		}else if(options[currentOption]=="Sair") {
			g.setColor(Color.RED);
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 25, 300 );
		}
	}
}
