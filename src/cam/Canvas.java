package cam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	protected double cam_x = 0, cam_y = 0;
	protected double cam_distance = 5;
	private int scale = 1000;
	protected double xrotation = 0;
	protected double yrotation = 0;
	protected double zrotation = 0;
	protected Point oldMousePosition = new Point();
	ArrayList<double[][]> points = new ArrayList<>();
	
	public Canvas() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		cam_x = -width/2;
		cam_y = -height/2;
		generatePoints(128);
		
		this.setBackground(Color.BLACK);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		renderCube(g);
	}
	
	public void renderCube(Graphics g) {
		
		double[][] rotation_x = {
			{1, 0, 0},
			{0, Math.cos(xrotation), -Math.sin(xrotation)},
			{0, Math.sin(xrotation), Math.cos(xrotation)}
		};
		
		double[][] rotation_y = {
			{Math.cos(yrotation), 0, Math.sin(yrotation)},
			{0, 1, 0},
			{-Math.sin(yrotation), 0, Math.cos(yrotation)}
		};
		
		double[][] rotation_z = {
			{Math.cos(zrotation), -Math.sin(zrotation), 0},
			{Math.sin(zrotation), Math.cos(zrotation), 0},
			{0, 0, 1}
		};
		
		ArrayList<int[]> projected_points = new ArrayList<>();
		for(double[][] point : points) {
			double[][] rotated_2d = Matrix.multiplyMatrix(rotation_x, point);
			rotated_2d = Matrix.multiplyMatrix(rotation_y, rotated_2d);
			rotated_2d = Matrix.multiplyMatrix(rotation_z, rotated_2d);
			
			double oz = 1/(cam_distance - rotated_2d[2][0]);
			double[][] orto_projection = {
				{oz, 0, 0},
				{0, oz, 0}
			};
			
			double[][] projected_point = Matrix.multiplyMatrix(orto_projection, rotated_2d);
			int px = (int) (projected_point[0][0] * scale - cam_x);
			int py = (int) (projected_point[1][0] * scale - cam_y);
			
			int[] ppoint = {px, py};
			projected_points.add(ppoint);
			
			g.drawOval(px, py, 1, 1);
		}
	}
	
	public void generatePoints(int a) {
		for(int i = 1; i <= a; ++i) {
			for(int j = 1; j <= a; ++j) {
				
				boolean edge = false;
				for(int k = 1; k <= a; ++k) {
					double x = mapValue(i, 1, a, -1, 1); 
					double y = mapValue(j, 1, a, -1, 1);
					double z = mapValue(k, 1, a, -1, 1);
					
					double[][] zeta = {{0}, {0}, {0}};
					
					int n = 6;
					int maxiterations = 10;
					int iteration = 0;
					
					while(true) {
						
						double[][] sz = triplexToSpherical(zeta[0][0], zeta[1][0], zeta[2][0]);
						
						double newx = Math.pow(sz[0][0], n) * Math.sin(sz[1][0]*n) * Math.cos(sz[2][0]*n);
						double newy = Math.pow(sz[0][0], n) * Math.sin(sz[1][0]*n) * Math.sin(sz[2][0]*n);
						double newz = Math.pow(sz[0][0], n) * Math.cos(sz[1][0]*n);
						
						zeta[0][0] = newx + x;
						zeta[1][0] = newy + y;
						zeta[2][0] = newz + z;
						iteration++;
						
						if(sz[0][0] > 1.6) {
							if(edge) {
								edge = false;
							}
							break;
						}
						
						if(iteration > maxiterations) {
							if(!edge) {
								edge = true;
								double[][] single_point = {{x},{y},{z}};
								points.add(single_point);
							}
							break;
						}
					}
				}
			}
		}
	}
	
	public double[][] triplexToSpherical(double x, double y, double z) {
		double r = Math.sqrt(x*x + y*y + z*z);
		double theta = Math.atan2(Math.sqrt(x*x + y*y), z);
		double phi = Math.atan2(y, x);
		return new double[][]{{r}, {theta}, {phi}};
	}
	
	public double mapValue(double num, int s, int e, int m, double n) {
	    return (num-s)/(double)(e-s) * (n-m) + m;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double rot_y = mapValue(e.getX() - oldMousePosition.x, 0, this.getHeight(), 0, 2*Math.PI);
		double rot_x = mapValue(e.getY() - oldMousePosition.y, 0, this.getWidth(), 0, -2*Math.PI);
		yrotation = (yrotation + rot_y)%(2*Math.PI);
		xrotation = (xrotation + rot_x)%(2*Math.PI);
		oldMousePosition.setLocation(e.getX(), e.getY());
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		oldMousePosition.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
