//Name: Nika Sharifi-Dariani
//Date: January 8th, 2018
//Description: The program will show 4 different coloured blocks on the screen. It will create a series of lights (a colour pattern), 
//and then it will require the user to remember and repeat the sequence. The program will continue to add onto this pattern until 
//the user gets one colour wrong. Once the user inputs the wrong colour in the pattern, the game will end. 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

//IMPORTANT NOTE:
//RED = 1 ; GREEN = 2 ; YELLOW = 3 ; BLUE = 4 ;
//SCREEN:
//= 0 IS MAIN MENU SCREEN ; = 1 IS ORIGINAL COLOURS ; = 2 IS MONOCHROMATIC COLOURS ; = 3 IS TROPICAL COLOURS
//= 4 IS GAME OVER SCREEN ; = 5 IS WINNER SCREEN
public class SimonGame extends JPanel implements MouseListener, ActionListener{
	//Customized Fonts & Colours Used
	private final Color REDFADE = new Color (180,0,0);
	private final Color GREENFADE = new Color (63,128,70);
	private final Color YELLOWFADE = new Color (201,175,40);
	private final Color BLUEFADE = new Color (36,49,150);
	private final Color PURPLE_1 = new Color (116,1,113);
	private final Color PURPLE_2 = new Color (167,88,162);
	private final Color PURPLE_3 = new Color (191,136,187);
	private final Color PURPLE_4 = new Color (221,191,218);
	private final Color PINK_TROP = new Color (249,129,162);
	private final Color GREEN_TROP = new Color (199,234,70);
	private final Color ORANGE_TROP = new Color (255,179,0);
	private final Color BLUE_TROP = new Color (115,194,251);
	private final Font ARIAL_BOLD = new Font ("Arial", Font.BOLD, 25);
	private final Font ARIAL_TEXT = new Font ("Arial" ,Font.PLAIN, 15);

	Image logoImage, rulesImage, gameoverImage, winnerImage;    //Imported Pictures
	static JFrame frame;
	
	AudioClip beep1, beep2, beep3, beep4; //Sound Effects
	AudioClip levelup;   
	AudioClip gameover;      
	AudioClip winner;


	int counter = 1;                //keeps track of repeats/level
	String code = "";				//code the program generates
	int score = 0;					//user's score
	int highest = 0;				//highest score
	int screen = 0;					//used to change the screens
	int x,y;						//keeps track of x and y coordinate of the mouse clicks
	boolean display = true;			//keeps track of when to display the code and when to get player's codeguess
	String codeguess = "" ;			//user's guess (input)


	public SimonGame(){
		setPreferredSize(new Dimension(600,480));
		setBackground(new Color(0,0,0));
		addMouseListener(this);
		
		//SIMON BOARDGAME IMAGE (SCREEN == 0)
		MediaTracker tracker = new MediaTracker (this);
		rulesImage = Toolkit.getDefaultToolkit().getImage("rules.jpg");
		tracker.addImage (rulesImage, 1);

		//SIMON LOGO IMAGE (SCREEN == 1,2,3)
		logoImage = Toolkit.getDefaultToolkit().getImage("logo.jpg");
		tracker.addImage (logoImage, 0);
		
		//GAME OVER IMAGE (SCREEN == 4)
		gameoverImage = Toolkit.getDefaultToolkit().getImage("gameover1.jpg");
		tracker.addImage (gameoverImage, 0);
		
		//WINNER IMAGE (SCREEN == 5)
		winnerImage = Toolkit.getDefaultToolkit().getImage("winner.jpg");
		tracker.addImage (winnerImage, 0);
		
		//MUSIC FILE VARIABLES
		beep1 = Applet.newAudioClip (getCompleteURL ("beep1.wav")); 
		beep2 = Applet.newAudioClip (getCompleteURL ("beep2.wav")); 
		beep3 = Applet.newAudioClip (getCompleteURL ("beep3.wav")); 
		beep4 = Applet.newAudioClip (getCompleteURL ("beep4.wav")); 
		levelup = Applet.newAudioClip (getCompleteURL ("levelup.wav")); 
		gameover = Applet.newAudioClip (getCompleteURL ("gameover.wav"));
		winner = Applet.newAudioClip (getCompleteURL ("winner.wav"));

		//MENU BAR SET UP 
		//Game Menu (New , Exit)
		JMenuItem newOption, exitOption;
		newOption = new JMenuItem ("New");
		exitOption = new JMenuItem ("Exit");
		JMenu gameMenu = new JMenu ("Game");
		
		gameMenu.add (newOption);
		gameMenu.addSeparator ();
		gameMenu.add(exitOption);

		//Colours Menu
		JMenuItem originalOption, monoOption, tropicalOption;
		originalOption = new JMenuItem ("Original");
		monoOption = new JMenuItem ("Monochromatic");
		tropicalOption = new JMenuItem ("Tropical");
		JMenu coloursMenu = new JMenu ("Colours");
		coloursMenu.add(originalOption);
		coloursMenu.addSeparator ();
		coloursMenu.add(monoOption);
		coloursMenu.addSeparator ();
		coloursMenu.add(tropicalOption);
		
		JMenuBar mainMenu = new JMenuBar ();
		mainMenu.add (gameMenu);
		mainMenu.add (coloursMenu);
		frame.setJMenuBar (mainMenu);
		//-----------------------------------------------------
		newOption.setActionCommand ("New");
		newOption.addActionListener (this);
		exitOption.setActionCommand ("Exit");
		exitOption.addActionListener (this);
		
		originalOption.setActionCommand ("Original");
		originalOption.addActionListener(this);
		monoOption.setActionCommand ("Monochromatic");
		monoOption.addActionListener(this);
		tropicalOption.setActionCommand ("Tropical");
		tropicalOption.addActionListener(this);
	} 
	//Description: Finds music files from computer
	//Parameters: Name of the file it should be looking for.
	//Return: does not return anything (null) 
	public URL getCompleteURL (String fileName)
	{
		try
		{
			return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
		}
		catch (MalformedURLException e)
		{
			System.err.println (e.getMessage ());
		}
		return null;
	}
	//Description: This method is used to prevent screens from lagging and checks what step 
	//the program is at (Ex: displaying the code or handling the user's input info)
	//Parameters: empty.
	//Return: void.
	public void animateButtons ()
	{
		Graphics g = getGraphics ();
		
		if (display == true){
			delay (1000);
			displayCode(g);
		}
		else if (display == false){
			handleAction(g);
		}
	}

	//Description: This method is in charge of the graphics of each screen. This is where the shapes and images are drawn/called.
	//Parameters: Graphics object is used to draw.
	//Return: void.
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		//MAIN MENU SCREEN (RULES)
		if (screen == 0) {
			g.clearRect (0, 0, this.getWidth (), this.getHeight ());
			g.setColor((Color.BLACK));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(rulesImage, 185, 10, this);
			g.setFont (ARIAL_BOLD);
			g.setColor(Color.WHITE);
			g.drawString("A game of memory skill!",150,245);
			g.setFont (ARIAL_TEXT);
			g.setColor(Color.WHITE);
			g.drawString("The game will show you a colour pattern.", 160, 270);
			g.drawString("Remember which colours were lit up. Once the ", 140, 300); 
			g.drawString("pattern is finished, repeat it to pass to the next level.", 130, 330);
			g.setFont (ARIAL_BOLD);
			g.setColor(Color.WHITE);
			g.drawString("PLAY", 250, 390);	
		}
		//ORIGINAL COLOURS
		else if (screen == 1){
			//Draws Game Titles (Text)
			g.drawImage (logoImage, 5, 30, this);
			g.setFont (ARIAL_BOLD);
//			g.setColor(Color.WHITE);
//			String scoreTitle = "Score: " + score;
//			String highestTitle = "Highest: " + highest;
//			g.drawString(scoreTitle, 420, 50);
//			g.drawString(highestTitle, 420, 80);
			g.setColor(Color.WHITE);
			g.drawString("Press to Start", 12, 100);
			
			//Draws Colours 
			g.setColor(REDFADE);
			g.fillArc(160, 100, 300, 300, 0, 90);
			g.setColor(GREENFADE);
			g.fillArc(150, 100, 300, 300, 90, 90);
			g.setColor(YELLOWFADE);
			g.fillArc(150, 110, 300, 300, 180, 90);
			g.setColor(BLUEFADE);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		//MONOCHROMATIC COLOURS
		else if (screen == 2){
			//Draws Game Titles (Text)
			g.drawImage (logoImage, 5, 30, this);
			g.setFont (ARIAL_BOLD);
//			g.setColor(Color.WHITE);
//			String scoreTitle = "Score: " + score;
//			String highestTitle = "Highest: " + highest;
//			g.drawString(scoreTitle, 420, 50);
//			g.drawString(highestTitle, 420, 80);
			g.setColor(Color.WHITE);
			g.drawString("Press to Start", 12, 100);
			
			//Draws Colours
			g.setColor(PURPLE_1);
			g.fillArc(160, 100, 300, 300, 0, 90);
			g.setColor(PURPLE_2);
			g.fillArc(150, 100, 300, 300, 90, 90);
			g.setColor(PURPLE_3);
			g.fillArc(150, 110, 300, 300, 180, 90);
			g.setColor(PURPLE_4);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		//TROPICAL COLOURS
		else if (screen == 3){
			//Draws Game Titles (Text)
			g.drawImage (logoImage, 5, 30, this);
			g.setFont (ARIAL_BOLD);
//			g.setColor(Color.WHITE);
//			String scoreTitle = "Score: " + score;
//			String highestTitle = "Highest: " + highest;
//			g.drawString(scoreTitle, 420, 50);
//			g.drawString(highestTitle, 420, 80);
			g.setColor(Color.WHITE);
			g.drawString("Press to Start", 12, 100);
			
			//Draws Colours
			g.setColor(PINK_TROP);
			g.fillArc(160, 100, 300, 300, 0, 90);
			g.setColor(GREEN_TROP);
			g.fillArc(150, 100, 300, 300, 90, 90);
			g.setColor(ORANGE_TROP);
			g.fillArc(150, 110, 300, 300, 180, 90);
			g.setColor(BLUE_TROP);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		//GAME OVER
		else if (screen == 4){
			g.drawImage (gameoverImage, 40, 50, this);
			g.setFont(ARIAL_BOLD);
			g.setColor(Color.YELLOW);
			g.drawString("BETTER LUCK NEXT TIME", 135, 270);
			String scoreTitle = "Your Score: " + score;
			String highestTitle = "Highest Score: " + highest;
			g.drawString(scoreTitle, 200, 300);
			g.drawString(highestTitle, 180, 330);
			g.setColor(Color.WHITE);
			g.drawString("PLAY", 250, 390);
		}
		//YOU WIN
		else if (screen == 5){
			g.drawImage (winnerImage, 0, 0, this);
			g.setFont(ARIAL_BOLD);
			g.setColor(Color.WHITE);
			String scoreTitle = "Your Score: " + score;
			String highestTitle = "Highest Score: " + highest;
			g.drawString(scoreTitle, 205, 330);
			g.drawString(highestTitle, 185, 360);
			g.drawString("PLAY", 250, 390);
		}

	}


	//Description: These four methods are in charge of changing the colour of the fillArc's in order to display the code.
	//Parameters: Graphics objects are used to draw.
	//Return: void.
	public void drawOne (Graphics g){
		beep1.play();
		if (screen == 1){
		g.setColor(Color.RED);
		g.fillArc(160, 100, 300, 300, 0, 90);
		}
		else if (screen == 2 || screen == 3){   //Colour Arc's will light up white for non traditional options because it was more convenient and shorter to code :)
		g.setColor(Color.WHITE);	
		g.fillArc(160, 100, 300, 300, 0, 90);
		}
		
		delay(500);

		if (screen == 1){
			g.setColor(REDFADE);
			g.fillArc(160, 100, 300, 300, 0, 90);
		}
		else if (screen == 2){
			g.setColor(PURPLE_1);
			g.fillArc(160, 100, 300, 300, 0, 90);
		}
		else if (screen == 3){
			g.setColor(PINK_TROP);
			g.fillArc(160, 100, 300, 300, 0, 90);
		}
	}

	public void drawTwo (Graphics g){
		beep2.play();
		if (screen == 1){
			g.setColor(Color.GREEN);
			g.fillArc(150, 100, 300, 300, 90, 90);
		}
		else if (screen == 2 || screen == 3){
			g.setColor(Color.WHITE);	
			g.fillArc(150, 100, 300, 300, 90, 90);
		}
		
		delay(500);
		
		if (screen == 1){
			g.setColor(GREENFADE);
			g.fillArc(150, 100, 300, 300, 90, 90);
		}
		else if (screen == 2){
			g.setColor(PURPLE_2);
			g.fillArc(150, 100, 300, 300, 90, 90);
		}
		else if (screen == 3){
			g.setColor(GREEN_TROP);
			g.fillArc(150, 100, 300, 300, 90, 90);
		}
	}

	public void drawThree (Graphics g){
		beep3.play();
		if (screen == 1){
			g.setColor(Color.YELLOW);
			g.fillArc(150, 110, 300, 300, 180, 90);
		}
		else if (screen == 2 || screen == 3){
			g.setColor(Color.WHITE);	
			g.fillArc(150, 110, 300, 300, 180, 90);
		}
		
		delay(500);
		
		if (screen == 1){
			g.setColor(YELLOWFADE);
			g.fillArc(150, 110, 300, 300, 180, 90);
		}
		else if (screen == 2){
			g.setColor(PURPLE_3);
			g.fillArc(150, 110, 300, 300, 180, 90);
		}
		else if (screen == 3){
			g.setColor(ORANGE_TROP);
			g.fillArc(150, 110, 300, 300, 180, 90);
		}

	}

	public void drawFour (Graphics g){
		beep4.play();
		if (screen == 1){
			g.setColor(Color.BLUE);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		else if (screen == 2 || screen == 3){
			g.setColor(Color.WHITE);
			g.fillArc(160, 110, 300, 300, 270, 90);	
		}
		
		delay(500);
		
		if (screen == 1){
			g.setColor(BLUEFADE);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		else if (screen == 2){
			g.setColor(PURPLE_4);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
		else if (screen == 3){
			g.setColor(BLUE_TROP);
			g.fillArc(160, 110, 300, 300, 270, 90);
		}
	}

	//Description: This method checks the code and calls the appropriate drawing method for that number/colour.
	//Parameters: Graphics objects are used to draw.
	//Return: void.
	public void displayCode (Graphics g){
		for (int i = 0 ; i < counter; i++){
			if (code.charAt(i) == '1'){
				drawOne(g);
				delay(500);
			}
			else if (code.charAt(i) == '2'){
				drawTwo(g);
				delay(500);
			}
			else if (code.charAt(i) == '3'){
				drawThree(g);
				delay(500);
			}
			else if (code.charAt(i) == '4'){
				drawFour(g);
				delay(500);	
			}
		}
		display = false;                   //display is equal to false when displaying code has finished
	}

	//Description: The method that is in charge of generating the code for the entire game by using the Math.random method.
	//Parameters: Represents the size of the code.
	//Return: void.
	public  void generateCode (int counter1){
		int randNum;
		for (int i = 0; i < counter1 ; i++){
			randNum = (int) (Math.random()*(4-1+1))+1;
			code = code + randNum;
		}
		System.out.println("code = " + code);
		animateButtons();
	}
	
	//Description: This method is in charge of keeping track of when score passes highest. When it does, it sets 
	//score as the new highest (high score).
	//Parameters: User's current game score.
	//Return: void.
	public  void countHighest (int score){
		if (score > highest){
			highest = score;
		}
	}
	//Description: This method resets key variables and restarts the game. 
	//Parameters: Empty.
	//Return: void.
	public void newGame (){
		counter = 1;
		code = "";
		score = 0;
		codeguess = "";
		System.out.println("counter = " + counter);
		delay (1000);
		levelup.play();
		delay (1000);
		display = true;
		generateCode(10);
	}
	
	//Description: This method is used to decipher what colour the mouse has clicked, then input that information 
	//into codeguess. After it will check both codes to see if they are equal or not.
	//Parameters: Graphics objects are used to draw.
	//Return: void.
	public void handleAction (Graphics g){
			//checks mouse location
			if ((x >= 311 && x <= 461) && (y >= 104 && y <= 254)){
				codeguess += "1";
				drawOne(g);
			}
			else if ((x >= 152 && x <= 301) && (y >= 104 && y <= 254)){
				codeguess += "2";
				drawTwo(g);
			}
			else if ((x >= 152 && x <= 301) && (y >= 263 && y <= 412)){
				codeguess += "3";
				drawThree(g);
			}
			else if ((x >= 311 && x <= 461) && (y >= 263 && y <= 412)){
				codeguess += "4";
				drawFour(g);
			}
		
		System.out.println ("guess = " + codeguess);
		//level up check: compares code to user's guess
		String codeTemp = "";
		if (codeguess.length() == counter){
			for (int i = 0; i < counter ; i++){
				codeTemp += code.charAt(i);	
			}

			System.out.println (counter + "****"+codeTemp);
			if (codeguess.length() == 10 && codeguess.equals(code)){
				screen = 5;
				score ++;
				countHighest(score);
				codeguess = "";
				codeTemp = "";
				code = "";
				winner.play();
			}
			else if(codeguess.equals(codeTemp)){ 
				codeTemp = "";
				codeguess = "";
				counter++;
				score++;
				countHighest(score);
				display = true;
				delay(1000);
				levelup.play();
				animateButtons();
			}
			else {
				System.out.println("GAME OVER");
				screen = 4;
				codeguess = "";
				codeTemp = "";
				code = "";
				gameover.play();
			}

		}

		repaint();

	}
	

	//Description: Program enters this method every time mouse is clicked. It is used for the picture buttons and calling animateButtons.
	//Parameters: MouseEvent objects store information about the mouse when it is clicked.
	//Return: void.
	public void mouseClicked (MouseEvent e)
	{
		x = e.getX ();
		y = e.getY ();
		//Rules screen photo button 'PLAY'
		if ((screen == 0 || screen == 4 || screen == 5) && (x > 250 && x < 320) && (y > 360 && y < 395)){     
			screen = 1;
			repaint();
		}
		//Game screen photo button 'Press to Start'
		else if (screen == 1 || screen == 2 || screen == 3){ 
			if ((x > 10 && x < 180) && (y > 80 && y < 105))                    
				newGame();
			animateButtons ();
		}
		

	}


	public void mouseReleased (MouseEvent e)
	{
	}


	public void mouseEntered (MouseEvent e)
	{
	}


	public void mouseExited (MouseEvent e)
	{
	}


	public void mousePressed (MouseEvent e)
	{
	}
	
	//Description: This method will change the screen value depending on the colours/game options that are selected from the menu bar.
	//Parameters: ActionEvent objects store information about the action that is chosen by the user (menu bar).
	//Return: void.
	public void actionPerformed (ActionEvent event){
		String eventName = event.getActionCommand();
		if (eventName.equals("Exit")){
			System.exit(0);
		}
		else if (eventName.equals("New")){
			screen = 0;
			repaint();
		}
		if (eventName.equals("Original")){
			screen = 1;
			repaint();
		}
		else if (eventName.equals("Monochromatic")){
			screen = 2;
			repaint();
		}
		else if (eventName.equals("Tropical")){
			screen = 3;
			repaint();
		}
	}

	//Description: Provides a delay in the program.
	//Parameters: Length of time to delay for.
	//Return: void.
	private void delay (int milliSec)
	{
		try
		{
			Thread.sleep (milliSec);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	//MAIN METHOD
	public static void main(String[] args) {
		frame = new JFrame ("Simon Game");
		SimonGame myPanel = new SimonGame();
		frame.add(myPanel);
		frame.pack();
		frame.setVisible(true);
	}


}
