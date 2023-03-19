package cam;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Main extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	Canvas canvas;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		canvas = new Canvas();
		
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.add(canvas);
		this.pack();
		this.addKeyListener(this);
		this.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_W -> {
				canvas.cam_distance-=0.1;
				repaint();
			}
			case KeyEvent.VK_S -> {
				canvas.cam_distance+=0.1;
				repaint();
			}
			case KeyEvent.VK_A -> {
				canvas.cam_x-=5;
				repaint();
			}
			case KeyEvent.VK_D -> {
				canvas.cam_x+=5;
				repaint();
			}
			case KeyEvent.VK_DOWN -> {
				canvas.cam_y-=5;
				repaint();
			}
			case KeyEvent.VK_UP -> {
				canvas.cam_y+=5;
				repaint();
			}
			case KeyEvent.VK_ESCAPE -> {
				dispose();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
