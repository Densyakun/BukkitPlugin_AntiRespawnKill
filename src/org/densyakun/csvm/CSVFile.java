package org.densyakun.csvm;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CSVFile implements Runnable {
	private File file;
	public CSVFile(File file) {
		this.file = file;
		new Thread(this).start();
	}
	public void setFile(File file) {
		this.file = file;
	}
	public File getFile() {
		return file;
	}
	public void AllWrite(List<List<String>>datas) throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		BufferedWriter w = new BufferedWriter(new FileWriter(file));
		for (int a = 0; a < datas.size(); a++) {
			List<String>l = datas.get(a);
			for (int b = 0; b < l.size(); b++) {
				w.write(l.get(b).trim());
				if (b != l.size() - 1) {
					w.write(", ");
				}
			}
			l = null;
			w.write("\n");
		}
		w.close();
		w = null;
	}
	public List<List<String>>AllRead() throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<List<String>>d = new ArrayList<List<String>>();
		BufferedReader r = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = r.readLine()) != null) {
			List<String> f = new ArrayList<String>();
			int b = 0;
			int e = 0;
			for (int c = 0; c < line.length(); c++) {
				Character h = Character.valueOf(line.charAt(c));
				if (h.equals('[')) {
					b++;
				} else if (h.equals(']')) {
					b--;
				} else if ((b == 0) && (h.equals(','))) {
					f.add(line.substring(e, c));
					e = c + 1;
				}
				h = null;
			}
			f.add(line.substring(e));
			for (int c = 0; c < f.size(); c++) {
				f.set(c, f.get(c).trim());
			}
			d.add(f);
			f = null;
		}
		line = null;
		r.close();
		r = null;
		return d;
	}
	public void LineWrite(List<String>line, int l) throws LineNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<List<String>>d = AllRead();
		if (d.size() < l + 1) {
			throw new LineNotFoundException(l);
		}
		d.set(l, line);
		AllWrite(d);
		d = null;
	}
	public List<String>LineRead(int l) throws LineNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<List<String>>d = AllRead();
		if (d.size() < l + 1) {
			throw new LineNotFoundException(l);
		}
		return d.get(l);
	}
	public void DataWrite(String data, int l, int c) throws LineNotFoundException, ColumnNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<String>d = LineRead(l);
		if (d.size() < c + 1) {
			throw new ColumnNotFoundException(l,c);
		}
		d.set(c, data);
		LineWrite(d, l);
		d = null;
	}
	public String DataRead(int l, int c) throws LineNotFoundException, ColumnNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<String>d = LineRead(l);
		if (d.size() < c + 1) {
			throw new ColumnNotFoundException(l, c);
		}
		return d.get(c);
	}
	public void addLine(List<String>line) throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<List<String>>d = AllRead();
		d.add(line);
		AllWrite(d);
		d = null;
	}
	public void addData(String data, int l) throws LineNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<String>d = LineRead(l);
		d.add(data);
		LineWrite(d, l);
		d = null;
	}
	public List<Point>getDatas(String data) throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		List<Point>d = new ArrayList<Point>();
		List<List<String>>a = AllRead();
		for (int b = 0; b < a.size(); b++) {
			for (int c = 0; c < a.get(b).size(); c++) {
				if (a.get(b).get(c).equalsIgnoreCase(data)) {
					d.add(new Point(c, b));
				}
			}
		}
		a = null;
		return d;
	}
	public boolean isCSVFile() throws IOException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		if (!file.isFile()) {
			throw new IOException("Not File.");
		}
		String[]n = file.getName().split("\\.", -1);
		if(n[n.length - 1].equalsIgnoreCase("csv")) {
			n = null;
			return true;
		}
		n = null;
		return false;
	}
	@Override
	public void run() {
	}
	public static String ArrayToString(String[] array) {
		if (array != null) {
			String a = "[";
			for (int b = 0; b < array.length; b++) {
				if (0 < b) {
					a += ", ";
				}
				a += array[b];
			}
			a += "]";
			return a;
		}
		return new String();
	}
	public static String ArrayToString(List<String> array) {
		if (array != null) {
			String a = "[";
			for (int b = 0; b < array.size(); b++) {
				if (0 < b) {
					a += ", ";
				}
				a += array.get(b);
			}
			a += "]";
			return a;
		}
		return new String();
	}
	public static List<String> StringtoArray(String str) {
		List<String> a = new ArrayList<String>();
		if (2 <= str.length()) {
			str = str.substring(1, str.length() - 1);
			int b = 0;
			int e = 0;
			for (int c = 0; c < str.length(); c++) {
				Character d = Character.valueOf(str.charAt(c));
				if (d.equals('[')) {
					b++;
				} else if (d.equals(']')) {
					b--;
				} else if ((b == 0) && (d.equals(','))) {
					a.add(str.substring(e, c));
					e = c + 1;
				}
				d = null;
			}
			a.add(str.substring(e));
			for (int c = 0; c < a.size(); c++) {
				a.set(c, a.get(c).trim());
			}
		}
		return a;
	}
	public static boolean isArray(String str) {
		if (0 < str.length()) {
			return (Character.valueOf(str.charAt(0)).equals('[')) && (Character.valueOf(str.charAt(str.length() - 1)).equals(']'));
		}
		return false;
	}
}
