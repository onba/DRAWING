package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FileChooserUI;

public class drawing{

	private JFrame frame;
	public static ArrayList<Color> ColorPalette = new ArrayList<Color>();
	private int pressedX;
	private int pressedY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					drawing window = new drawing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public drawing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void PaletteDrawing(JPanel palette) {
		palette.setBounds(1100, 60, 75, 30 * ColorPalette.size() + 6);
		Graphics g = palette.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(3, 3, 30, drawing.ColorPalette.size() * 30);
		for (int i = 0; i < drawing.ColorPalette.size(); i++) {
			g.setColor(drawing.ColorPalette.get(i));
			g.fillRect(4, 30 * i + 4, 28, 28);
			g.setColor(Color.WHITE);
			g.fillRect(60, 30 * i + 13, 10, 10);
			if (drawing.ColorPalette.get(i).equals(PaintingSpace.getFirstColor()))
				g.setColor(Color.RED);
			g.fillRect(40, 30 * i + 13, 10, 10);
		}
	}
	public void AreaFills (Graphics g, Color FieldColor, Color NewColor, int i, int j){
			g.setColor(NewColor);
			g.fillRect(i*PaintingSpace.getSizeX()+6, j*PaintingSpace.getSizeY()+6, 29, 29);
			PaintingSpace.DrawField(i, j, PaintingSpace.getFirstColor());
			PaintingSpace.setModified(i, j, true);
			if (i<PaintingSpace.getCountX()-1 && !PaintingSpace.getModified(i+1, j) && PaintingSpace.getFieldColor(i+1, j).equals(FieldColor)) AreaFills(g, FieldColor, NewColor, i+1, j);
			if (j<PaintingSpace.getCountY()-1 && !PaintingSpace.getModified(i, j+1) &&PaintingSpace.getFieldColor(i, j+1).equals(FieldColor))AreaFills(g, FieldColor, NewColor, i, j+1);
			if (i>0 && !PaintingSpace.getModified(i-1, j) &&PaintingSpace.getFieldColor(i-1, j).equals(FieldColor))AreaFills(g, FieldColor, NewColor, i-1, j);
			if (j>0 && !PaintingSpace.getModified(i, j-1) &&PaintingSpace.getFieldColor(i, j-1).equals(FieldColor))AreaFills(g, FieldColor, NewColor, i, j-1);
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(0, 0, 1604, 1040);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		PaintingSpace ps = new PaintingSpace(30, 30, 32, 32, Color.BLACK);

		/* N�h�ny alapsz�nt hozz�adunk a palett�nkhoz. */
		ColorPalette.add(Color.RED);
		ColorPalette.add(Color.BLACK);
		ColorPalette.add(Color.BLUE);
		ColorPalette.add(Color.CYAN);
		ColorPalette.add(Color.MAGENTA);
		ColorPalette.add(Color.GREEN);
		ColorPalette.add(Color.YELLOW);
		ColorPalette.add(Color.LIGHT_GRAY);
		ColorPalette.add(Color.ORANGE);
		ColorPalette.add(Color.WHITE);

		JPanel palette = new Palette();

		JPanel addColor = new JPanel();
		addColor.setBounds(1221, 60, 304, 232);
		frame.getContentPane().add(addColor);
		addColor.setLayout(null);

		JSlider redslider = new JSlider();
		redslider.setValue(0);
		redslider.setMaximum(255);
		redslider.setBounds(56, 18, 140, 26);
		addColor.add(redslider);

		JSlider greenslider = new JSlider();
		greenslider.setValue(0);
		greenslider.setMaximum(255);
		greenslider.setBounds(56, 65, 140, 26);
		addColor.add(greenslider);

		JSlider blueslider = new JSlider();
		blueslider.setValue(0);
		blueslider.setMaximum(255);
		blueslider.setBounds(56, 110, 140, 26);
		addColor.add(blueslider);

		palette.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouse) {
				if (mouse.getX() < 30 && mouse.getX() > 0 && mouse.getY() % 30 != 3 && mouse.getY() > 3) {
					/*
					 * Csak akkor l�p�nk be, ha a megfelel� tartom�nyban van az
					 * eg�r, illetve ha m�r nem els�dleges illetve m�sodlagos
					 * sz�n a v�lasztott.
					 */
					if (!ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30)
							.equals(PaintingSpace.getSecondColor())
							&& !ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30)
									.equals(PaintingSpace.getFirstColor())) {
						Graphics g = palette.getGraphics();
						g.setColor(Color.WHITE);
						for (int i = 0; i < ColorPalette.size(); i++) {
							/*
							 * Ez az egyenlet teszi lehet�v�, hogy amelyiket nem
							 * v�ltoztatjuk meg (els�dleges, vagy m�sodlagos) ne
							 * legyen feh�r
							 */
							if ((mouse.getButton() != 1 || !PaintingSpace.getSecondColor().equals(ColorPalette.get(i)))
									&& (mouse.getButton() != 3
											|| !PaintingSpace.getFirstColor().equals(ColorPalette.get(i))))
								g.fillRect(40, 30 * i + 13, 10, 10);
						}
						/*
						 * redslider.setValue(ColorPalette.get((mouse.getY() - 3
						 * - (mouse.getY() - 3) % 30) / 30).getRed());
						 * greenslider.setValue(ColorPalette.get((mouse.getY() -
						 * 3 - (mouse.getY() - 3) % 30) / 30).getGreen());
						 * blueslider.setValue(ColorPalette.get((mouse.getY() -
						 * 3 - (mouse.getY() - 3) % 30) / 30).getBlue());
						 */
						if (mouse.getButton() == 1
								&& !ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30)
										.equals(PaintingSpace.getSecondColor())) {
							PaintingSpace
									.Change1Color(ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30));
							g.setColor(Color.RED);
						} else if (mouse.getButton() == 3
								&& !ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30)
										.equals(PaintingSpace.getFirstColor())) {
							PaintingSpace
									.Change2Color(ColorPalette.get((mouse.getY() - 3 - (mouse.getY() - 3) % 30) / 30));
							g.setColor(Color.BLUE);
						}
						g.fillRect(40, mouse.getY() - 3 - (mouse.getY() - 3) % 30 + 13, 10, 10);
					}
				}
			}
		});
		palette.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent mouse) {
				Graphics g = palette.getGraphics();
				g.setColor(Color.WHITE);
				for (int i = 0; i < ColorPalette.size(); i++) {
					g.fillRect(60, 30 * i + 13, 10, 10);
				}
				if (mouse.getX() < 30 && mouse.getX() > 0 && mouse.getY() % 30 != 3 && mouse.getY() > 3) {
					g.setColor(Color.GREEN);
					g.fillRect(60, mouse.getY() - 3 - (mouse.getY() - 3) % 30 + 13, 10, 10);
				}
			}
		});
		palette.setBounds(1100, 60, 75, 30 * ColorPalette.size() + 6);
		frame.getContentPane().add(palette);

		JPanel canvas = new Canvas();
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent mouse) {
				int FieldX = (mouse.getX() - 5 - (mouse.getX() - 5) % PaintingSpace.getSizeX()) / PaintingSpace.getSizeX();
				int FieldY = (mouse.getY() - 5 - (mouse.getY() - 5) % PaintingSpace.getSizeY()) / PaintingSpace.getSizeY();
				/* Be�ll�tjuk, hogy t�roljuk a mez� sz�n�t. */
				Graphics g = canvas.getGraphics();
				if (mouse.getButton() == 1)
					g.setColor(PaintingSpace.getFirstColor());
				if (mouse.getButton() == 3)
					g.setColor(PaintingSpace.getSecondColor());
				/*if (mouse.getButton() == 2) {
					g.setColor(PaintingSpace.getSecondColor());
					PaintingSpace.ChangeFColor(PaintingSpace.getSecondColor());
						for (int i=0 ; i<=PaintingSpace.getCountX(); i++)
							g.drawLine(i*PaintingSpace.getSizeX()+5, 5, i*PaintingSpace.getSizeX()+5, PaintingSpace.getCountY()*PaintingSpace.getSizeY()+5);
					//	v�zszintes r�csok
					for (int j=0 ; j<=PaintingSpace.getCountY();j++)
							g.drawLine(5, j*PaintingSpace.getSizeY()+5, PaintingSpace.getCountX()*PaintingSpace.getSizeX()+5, j*PaintingSpace.getSizeY()+5);
					//	f�gg�leges r�csok
				}
				else{*/
				if (mouse.getButton()==2){
					AreaFills(g, PaintingSpace.getFieldColor(FieldX, FieldY), PaintingSpace.getFirstColor(), FieldX, FieldY);
					for (int i=0;i<PaintingSpace.getCountX();i++)
						for (int j=0; j<PaintingSpace.getCountY();j++)
							PaintingSpace.setModified(i, j, false);
				} else {
				for (int i = Math.min(pressedX,FieldX); i <= Math.max(pressedX,FieldX); i++)
					for (int j = Math.min(pressedY,FieldY); j <= Math.max(pressedY,FieldY); j++){
						g.fillRect(i * PaintingSpace.getSizeX() + 6, j * PaintingSpace.getSizeY() + 6, PaintingSpace.getSizeX() - 1, PaintingSpace.getSizeY() - 1);
						if (mouse.getButton()==1) PaintingSpace.DrawField(i, j, PaintingSpace.getFirstColor());
						if (mouse.getButton()==3) PaintingSpace.DrawField(i, j, PaintingSpace.getSecondColor());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent mouse) {
				pressedX = (mouse.getX() - 5 - (mouse.getX() - 5) % PaintingSpace.getSizeX()) / PaintingSpace.getSizeX();
				pressedY = (mouse.getY() - 5 - (mouse.getY() - 5) % PaintingSpace.getSizeY()) / PaintingSpace.getSizeY();
				}
		});
		canvas.setBounds(20, 20, PaintingSpace.getCountX() * PaintingSpace.getSizeX() + 10,
				PaintingSpace.getCountY() * PaintingSpace.getSizeY() + 10);
		frame.getContentPane().add(canvas);

		redslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Graphics g = addColor.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(212, 57, 56, 56);
				g.setColor(new Color(redslider.getValue(), greenslider.getValue(), blueslider.getValue()));
				g.fillRect(215, 60, 50, 50);
			}
		});
		blueslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Graphics g = addColor.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(212, 57, 56, 56);
				g.setColor(new Color(redslider.getValue(), greenslider.getValue(), blueslider.getValue()));
				g.fillRect(215, 60, 50, 50);
			}
		});
		greenslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Graphics g = addColor.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(212, 57, 56, 56);
				g.setColor(new Color(redslider.getValue(), greenslider.getValue(), blueslider.getValue()));
				g.fillRect(215, 60, 50, 50);
			}
		});

		JButton Random = new JButton("Random");
		Random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Random rand = new Random();
				int redi = rand.nextInt(256);
				int greeni = rand.nextInt(256);
				int bluei = rand.nextInt(256);
				redslider.setValue(redi);
				greenslider.setValue(greeni);
				blueslider.setValue(bluei);
				Graphics g = addColor.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(212, 57, 56, 56);
				g.setColor(new Color(redi, greeni, bluei));
				g.fillRect(215, 60, 50, 50);
			}
		});
		Random.setBounds(205, 154, 89, 23);
		addColor.add(Random);
		JLabel lblRed = new JLabel("Red");
		lblRed.setFont(new Font("Arial", Font.BOLD, 14));
		lblRed.setBounds(12, 24, 28, 14);
		addColor.add(lblRed);

		JLabel lblGreen = new JLabel("Green");
		lblGreen.setFont(new Font("Arial", Font.BOLD, 14));
		lblGreen.setBounds(5, 70, 46, 14);
		addColor.add(lblGreen);

		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setFont(new Font("Arial", Font.BOLD, 14));
		lblBlue.setBounds(12, 116, 32, 14);
		addColor.add(lblBlue);

		JButton btnRandompicture = new JButton("RandPicture");
		btnRandompicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color OriginalColor = PaintingSpace.getFirstColor();
				Random rand = new Random();
				int red, green, blue;
				Graphics g = canvas.getGraphics();
				for (int i = 0; i < PaintingSpace.getCountX(); i++) {
					for (int j = 0; j < PaintingSpace.getCountY(); j++) {
						red = rand.nextInt(256);
						green = rand.nextInt(256);
						blue = rand.nextInt(256);
						PaintingSpace.DrawField(i, j, new Color(red, green, blue));
						PaintingSpace.Change1Color(new Color(red,green,blue));
						/* Be�ll�tjuk, hogy t�roljuk a mez� sz�n�t. */
						g.setColor(PaintingSpace.getFirstColor());
						g.fillRect(i * PaintingSpace.getSizeX() + 6, j * PaintingSpace.getSizeY() + 6, PaintingSpace.getSizeX() - 1, PaintingSpace.getSizeY() - 1);
					}
				}
				PaintingSpace.Change1Color(OriginalColor);
			}
		});
		btnRandompicture.setBounds(14, 154, 110, 23);
		addColor.add(btnRandompicture);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean exist = false;
				Color newColor = new Color(redslider.getValue(), greenslider.getValue(), blueslider.getValue());
				for (int i = 0; i < ColorPalette.size(); i++)
					if (newColor.equals(ColorPalette.get(i)))
						exist = true;
				if (!exist) {
					ColorPalette.add(newColor);
					PaletteDrawing(palette);
				}
			}
		});
		btnAdd.setBounds(134, 154, 60, 23);
		addColor.add(btnAdd);
		
		JButton btnSacepicture = new JButton("SavePicture");
		btnSacepicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 ColorModel cm = ColorModel.getRGBdefault();
				    int w = PaintingSpace.getCountX()*PaintingSpace.getSizeX();
				    int h = PaintingSpace.getCountY()*PaintingSpace.getSizeY();
				    WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
				    int[] col = new int[4];
				    int[] fcol = new int[4];
				    fcol[0]=PaintingSpace.getFrameColor().getRed();
		    		fcol[1]=PaintingSpace.getFrameColor().getGreen();
		    		fcol[2]=PaintingSpace.getFrameColor().getBlue();
				    fcol[3]=255;
		    		col[3]=255;
				    try{
				    for (int i=0; i<PaintingSpace.getCountX();i++){
				    	for (int j=0; j<PaintingSpace.getCountY();j++){
				    		col[0]=PaintingSpace.getFieldColor(i, j).getRed();
				    		col[1]=PaintingSpace.getFieldColor(i, j).getGreen();
				    		col[2]=PaintingSpace.getFieldColor(i, j).getBlue();
				    		for (int x=0;x<PaintingSpace.getSizeX();x++){
				    			for (int y=0;y<PaintingSpace.getSizeY();y++){
				    				if (x==PaintingSpace.getSizeX()-1 || y==PaintingSpace.getSizeY()-1) raster.setPixel(i*PaintingSpace.getSizeX()+x, j*PaintingSpace.getSizeY()+y, fcol);
				    				else raster.setPixel(i*PaintingSpace.getSizeX()+x, j*PaintingSpace.getSizeY()+y, col);
				    	}
				    		}
				    	}
				    }
				    BufferedImage image = new BufferedImage(cm, raster, false, null);
				    File output = new File("output.jpg");
				    FileOutputStream fos = new FileOutputStream(output.getAbsolutePath());
				    ImageIO.write(image, "PNG", fos);
				    fos.close();
				    } catch (Exception ex){
				    	ex.printStackTrace();
				    }
			}
		});
		btnSacepicture.setBounds(14, 190, 110, 23);
		addColor.add(btnSacepicture);
		
		JButton btnLoad = new JButton("LoadPic");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
		            BufferedImage bi = ImageIO.read(new File("input.jpg"));
		            int w = bi.getWidth(null);
		            int h = bi.getHeight(null);
		            int newCountX=w*PaintingSpace.getCountX()/h;
		            int newSizeX=PaintingSpace.getCountX()*PaintingSpace.getSizeX()/newCountX;
		            PaintingSpace.setCountX(newCountX);
		            PaintingSpace.setSizeX(newSizeX);
		            Raster r = bi.getData();
		            int[] col = new int[4];
		            Color rgb;
		            for (int i=0;i<PaintingSpace.getCountX();i++){
		            	for (int j=0;j<PaintingSpace.getCountY();j++){
		            		col = r.getPixel(i*w/PaintingSpace.getCountX(), j*h/PaintingSpace.getCountY(), col);
		            		rgb=new Color(col[0],col[1],col[2]);
		            		PaintingSpace.DrawField(i, j, rgb);
		            	}
		            }
		            DrawImage(canvas.getGraphics());
		        } catch (IOException e) {
		        	JOptionPane.showMessageDialog(canvas, "You need to paste the file in the root folder and name by input.jpg");
		            System.out.println("Image could not be read");
		        }
			}
		});
		btnLoad.setBounds(205, 188, 89, 23);
		addColor.add(btnLoad);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, frame.getWidth(), 21);
		frame.getContentPane().add(menuBar);

		JMenu mnGame = new JMenu("Frame");
		menuBar.add(mnGame);
		
		JRadioButtonMenuItem rdbtnmntmBlue = new JRadioButtonMenuItem("Blue");
		mnGame.add(rdbtnmntmBlue);
		
		JRadioButtonMenuItem rdbtnmntmBlack = new JRadioButtonMenuItem("Black");
		mnGame.add(rdbtnmntmBlack);
		
		JRadioButtonMenuItem rdbtnmntmGreen = new JRadioButtonMenuItem("Green");
		mnGame.add(rdbtnmntmGreen);
	}
	private void DrawImage(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, PaintingSpace.getCountX()*PaintingSpace.getSizeX()+10, PaintingSpace.getCountY()*PaintingSpace.getSizeY()+10);
		for (int i=0; i<PaintingSpace.getCountX(); i++){
			for (int j=0; j<PaintingSpace.getCountY(); j++){
				g.setColor(PaintingSpace.getFieldColor(i, j));
				g.fillRect(i * PaintingSpace.getSizeX() + 6, j * PaintingSpace.getSizeY() + 6, PaintingSpace.getSizeX() - 1, PaintingSpace.getSizeY() - 1);
			}
		}
		// mez�k
		g.setColor(PaintingSpace.getFrameColor());
		for (int i=0 ; i<=PaintingSpace.getCountX(); i++)
				g.drawLine(i*PaintingSpace.getSizeX()+5, 5, i*PaintingSpace.getSizeX()+5, PaintingSpace.getCountY()*PaintingSpace.getSizeY()+5);
		//	v�zszintes r�csok
		for (int j=0 ; j<=PaintingSpace.getCountY();j++)
				g.drawLine(5, j*PaintingSpace.getSizeY()+5, PaintingSpace.getCountX()*PaintingSpace.getSizeX()+5, j*PaintingSpace.getSizeY()+5);
		//	f�gg�leges r�csok	
		g.setColor(Color.BLACK);
		g.fillRect(PaintingSpace.getCountX()*PaintingSpace.getSizeX()+6, 0, PaintingSpace.getSizeX(), PaintingSpace.getCountY() * PaintingSpace.getSizeY() + 10);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
