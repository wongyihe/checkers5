package checkers;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class board extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int[][] A = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 4, 0, 0, 0, 0 },
			{ 0, 0, 0, 3, 3, 1, 1, 1, 1, 1, 1, 4, 4, 0, 0, 0, 0 },
			{ 0, 0, 3, 3, 3, 1, 1, 1, 1, 1, 4, 4, 4, 0, 0, 0, 0 },
			{ 0, 3, 3, 3, 3, 1, 1, 1, 1, 4, 4, 4, 4, 0, 0, 0, 0 },
			{ 3, 3, 3, 3, 3, 1, 1, 1, 4, 4, 4, 4, 4, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	int[][][] goal = { {}, {}, {} };
	double l = 40;
	double dx = l * Math.cos(60 * Math.PI / 180);
	double dy = l * Math.sin(60 * Math.PI / 180);
	int r = 5;
	Point p0 = new Point();
	int mark = 0;
	int i_old, j_old;
	int myColor = 3;
	board b;
	Stack<Point> path = new Stack<Point>();
	HashSet<Point> path_his = new HashSet<Point>();
	SocketClient sc = new SocketClient();

	int[][] move_jump = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 }, { -2, 2 }, { 2, -2 } };
	int[][] move_step = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, -1 } };

	void clearmark() {
		int i, j;
		for (i = 0; i < 17; i++)
			for (j = 0; j < 17; j++) {
				if (A[i][j] > 4)
					A[i][j] = 1;
			}
		repaint();
	}

	void possiblemove(int i, int j) {
		int k, l;
		for (int m = 0; m < 6; m++) {
			k = i + move_jump[m][0];
			l = j + move_jump[m][1];
			if (k >= 0 && l >= 0 && k < 17 && l < 17)
				if (ismovable(i, j, k, l)) {
					A[k][l] = 5;
					possiblemove(k, l);
				}
			k = i + move_step[m][0];
			l = j + move_step[m][1];
			if (k >= 0 && l >= 0 && k < 17 && l < 17)
				if (ismovable(i, j, k, l) && A[i][j] != 5) {
					A[k][l] = 5;
				}
		}
		repaint();
	}

	boolean ismovable(int i, int j, int k, int l) {

		if (i >= 0 && i < 17 && j >= 0 && j < 17 && k >= 0 && k < 17 && l >= 0 && l < 17) {
			if (A[i][j] > 1 && A[k][l] == 1) {
				if ((k == i - 1 && l == j) || (k == i + 1 && l == j) || (k == i && l == j - 1) || (k == i && l == j + 1)
						|| (k == i - 1 && l == j + 1) || (k == i + 1 && l == j - 1))
					return true;
			}
			if (A[i][j] > 1 && A[k][l] == 1) {
				if ((k == i - 1 && l == j) || (k == i + 1 && l == j) || (k == i && l == j - 1) || (k == i && l == j + 1)
						|| (k == i - 1 && l == j + 1) || (k == i + 1 && l == j - 1))
					return true;
				if (((k == i - 2 && l == j) && A[i - 1][l] > 1) || ((k == i + 2 && l == j) && A[i + 1][j] > 1)
						|| ((k == i && l == j - 2) && A[i][j - 1] > 1) || ((k == i && l == j + 2) && A[i][j + 1] > 1)
						|| ((k == i - 2 && l == j + 2) && A[i - 1][j + 1] > 1)
						|| ((k == i + 2 && l == j - 2) && A[i + 1][j - 1] > 1))
					return true;
			}
		}
		return false;
	}

	// -------------sent message---------
	String GetPath(int i_old, int j_old, int i, int j) {
		String s = "";
		path.clear();
		path_his.clear();
		Point target_p = new Point(i_old, j_old);
		Point current_p = new Point(i, j);
		path.push(current_p);
		System.out.println("target_p:" + target_p.getX() + "," + target_p.getY());
		System.out.println("0000push:" + current_p.getX() + "," + current_p.getY());
		path_his.add(current_p);
		possiblepath(target_p, i, j);
		s = A[i][j] + "," + PrintPath();
		return s;
	}

	String PrintPath() {
		String s_path = "";
		for (int i = path.size() - 1; i >= 0; i--) {
			s_path += "," + (int) (path.get(i).getX()) + "," + (int) (path.get(i).getY());
		}
		return (path.size() - 1) + s_path;

	}

	void possiblepath(Point target_p, int i, int j) {

		int k, l;
		// ---------step--------
		for (int m = 0; m < 6; m++) {
			k = i + move_step[m][0];
			l = j + move_step[m][1];
			Point step_next_p = new Point(k, l);
			if (k >= 0 && l >= 0 && k < 17 && l < 17)

				if (ismovable(i, j, k, l) && step_next_p.equals(target_p)) {
					// path.push(step_next_p);
					path.push(target_p);
					return;
				}
		}
		// -----------jump-------
		for (int m = 0, times = 0; m < 6; m++) {

			k = i + move_jump[m][0];
			l = j + move_jump[m][1];

			System.out.println(i + "," + j + "to" + k + "," + l);
			System.out.println("ismovalbe:" + ismovable(i, j, k, l));
			Point next_p = new Point(k, l);
			Point current_p = new Point(i, j);
			if (k >= 0 && l >= 0 && k < 17 && l < 17)

				if (current_p.equals(target_p)) {
					return;
				}
			// 從當前這點到下一點是可行的，下一點不等於終點，下一點沒走過
			if (check(i, j, k, l) && !next_p.equals(target_p) && path_his.add(next_p)) {// && path_his.add(next_p)
				path.push(next_p);
				System.out.println("1111push:" + next_p.getX() + "," + next_p.getY());
				possiblepath(target_p, k, l);
			}
			// 
			else if (check(i, j, k, l) && next_p.equals(target_p) && path_his.add(next_p)) {//

				path.push(next_p);
				System.out.println("2222push:" + next_p.getX() + "," + next_p.getY());
				return;
			} else {
				times++;
				if (times == 6) {
					try {
						System.out.println("3333pop:" + path.peek().getX() + "," + path.peek().getY());
						path.pop();
					} catch (Exception e) {
					}
				}
			}

		}

	}

	// ---------------------------
	public board() {
		for (int i = 0; i < 17; i++)
			for (int j = 0; j < 17; j++)
				if (A[i][j] == 4)
					A[i][j] = 1;

		p0.x = 150;
		p0.y = 40;
		b = this;
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int i = (int) Math.round((x - p0.x - (y - p0.y) * dx / dy) / 2 / dx);
				int j = (int) Math.round((y - p0.y) / dy);

				if (A[i][j] == 5) {
					A[i][j] = mark;
					A[i_old][j_old] = 1;
					b.repaint();
					clearmark();
					sc.sent(GetPath(i_old, j_old, i, j));
				}
				clearmark();
				if (iswin(A[i][j])) {
					sc.sent(A[i][j] + " win!!");
					System.out.println(A[i][j] + " win!");
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int i = (int) Math.round((x - p0.x - (y - p0.y) * dx / dy) / 2 / dx);
				int j = (int) Math.round((y - p0.y) / dy);
				mark = A[i][j];
				i_old = i;
				j_old = j;
				if (A[i][j] == myColor)
					possiblemove(i, j);
				b.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final int N = 17;
		int i, j;
		cell c = new cell();
		int x, y;
		for (i = 0; i < N; i++)
			for (j = 0; j < N; j++) {
				if (A[i][j] > 0) {
					x = (int) ((2 * i + j) * dx);
					y = (int) (j * dy);
					c.set(p0.x + x, p0.y + y, A[i][j] - 1);
					c.draw(g);
					g.drawString("(" + i + "," + j + ")", p0.x + x, p0.y + y + r + r + r);
				}
			}
	}

	boolean set(int i, int j, int player) {
		if (A[i][j] < 1) {
			System.out.println("wrong position");
			return false;
		} else
			A[i][j] = player;
		return true;
	}

	// -----------receive message----------
	boolean jump(int i, int j, int m, int n) {
		if (ismovable(i, j, m, n)) {
			System.out
					.println("Before mn:" + m + "," + n + ": " + A[m][n] + "to" + "ij:" + i + "," + j + ": " + A[i][j]);
			// A[i][j] = A[m][n];
			// A[m][n] = 1;
			System.out
					.println("After mn:" + m + "," + n + ": " + A[m][n] + "to" + "ij:" + i + "," + j + ": " + A[i][j]);
			A[m][n] = A[i][j];
			A[i][j] = 1;
			repaint();
			System.out.println("repainted!!!");
		}
		return true;
	}

	boolean jump(ArrayList<Integer> command) {
		Vector<Point> p = new Vector<Point>();
		for (int i = 2; i < command.size(); i += 2)
			p.add(new Point((int) command.get(i), (int) command.get(i + 1)));

		int times = (int) command.get(1);
		int cur_step = times;
		int i = (int) command.get(2);
		int j = (int) command.get(3);
		int m = (int) command.get(command.size() - 2);
		int n = (int) command.get(command.size() - 1);

		if (checkPath(p, times, cur_step) && A[i][j] != myColor) {
			A[m][n] = A[i][j];
			A[i][j] = 1;
			repaint();
			if (iswin(A[m][n])) {
				sc.sent("you win:((");
			}
			return true;
		} else {
			return false;
		}
	}
	
	boolean checkPath(Vector<Point> p, int times, int cur_step) {
		boolean cango = check(p.get(times - cur_step).x, p.get(times - cur_step).y, p.get(times - cur_step + 1).x,
				p.get(times - cur_step + 1).y);
		
		if (cur_step > 0 && cango) {
			System.out.println("hi there");
			cur_step--;
			if (cur_step == 0)
				return true;
			boolean checked = checkPath(p, times, cur_step);
			return checked;

		} else {
			System.out.println("nonono!!!");
			return false;
		}
	}

	boolean check(int i, int j, int k, int l) {
		if (i >= 0 && i < A.length && j >= 0 && j < A[0].length && k >= 0 && k < A.length && l >= 0
				&& l < A[0].length) {
			if (A[k][l] == 1) {
				if ((k == i - 1 && l == j) || (k == i + 1 && l == j) || (k == i && l == j - 1) || (k == i && l == j + 1)
						|| (k == i - 1 && l == j + 1) || (k == i + 1 && l == j - 1))
					return true;
			}
			if (A[k][l] == 1) {
				if ((k == i - 1 && l == j) || (k == i + 1 && l == j) || (k == i && l == j - 1) || (k == i && l == j + 1)
						|| (k == i - 1 && l == j + 1) || (k == i + 1 && l == j - 1))
					return true;
				if (((k == i - 2 && l == j) && A[i - 1][l] > 1) || ((k == i + 2 && l == j) && A[i + 1][j] > 1)
						|| ((k == i && l == j - 2) && A[i][j - 1] > 1) || ((k == i && l == j + 2) && A[i][j + 1] > 1)
						|| ((k == i - 2 && l == j + 2) && A[i - 1][j + 1] > 1)
						|| ((k == i + 2 && l == j - 2) && A[i + 1][j - 1] > 1))
					return true;
			}
		}
		return false;
	}

	// ----------check if win----------
	boolean iswin(int color) {
		int[] c = center(color);
		if (color == 2) {
			if (c[0] == 200 && c[1] == 80)
				return true;
			else
				return false;
		} else if (color == 3) {
			if (c[0] == 80 && c[1] == 200)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	int[] center(int color) {
		int X = 0;
		int Y = 0;
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				if (A[i][j] == color) {
					X += i;
					Y += j;
				}
			}
		}
		int[] c = { X, Y };
		return c;
	}
}

class cell {
	Point p = new Point();
	int r = 10;
	int player = 0;
	final Color[] c = { Color.WHITE, Color.YELLOW, Color.RED, Color.GREEN, Color.GRAY };

	void set(int x, int y, int player) {
		p.x = x;
		p.y = y;
		this.player = player;
	}

	void draw(Graphics g) {
		g.setColor(c[player]);
		g.fillOval(p.x - r, p.y - r, r + r, r + r);
		g.setColor(Color.BLACK);
		g.drawOval(p.x - r, p.y - r, r + r, r + r);
	}

}