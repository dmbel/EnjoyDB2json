package ru.enjoy.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.data.Root;
import ru.enjoy.server.exceptions.BadDataAnnotationException;
import ru.enjoy.server.objbuilder.IArrayReceiver;
import ru.enjoy.server.objbuilder.ObjectBuilder;

public class App {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out
					.println("Error! В строке запуска EnjoyDB2json, должны быть "
							+ "указаны 2 параметра: url для запроса базы и путь к "
							+ "файлу в который записать json данные");
			return;
		}
		URL url;
		HttpURLConnection conn;
		ObjectBuilder ob;
		InputStream bdIn = null;
		PrintWriter wr = null;
		try {
			url = new URL(args[0]);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			bdIn = conn.getInputStream();
			// Чтобы в начало и в конец добавить обрамляющие <root> и </root>
			// используем тройной поток через SequenceInputStream
			List<InputStream> stream3 = Arrays.asList(
					new ByteArrayInputStream("<root>".getBytes(Charset
							.forName("UTF-8"))),
					bdIn,
					new ByteArrayInputStream("</root>".getBytes(Charset
							.forName("UTF-8"))));
			InputStream in = new SequenceInputStream(
					Collections.enumeration(stream3));
			// InputStream in = new FileInputStream(args[0]);
			ob = new ObjectBuilder(new Root());
			DataBase2Object loader = new DataBase2Object();

			loader.load(in, ob);
			ob.makeStruct();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// OutputStream out = new FileOutputStream(args[1]);
			wr = new PrintWriter(new GZIPOutputStream(new FileOutputStream(args[1])));
			wr.print(gson.toJson(ob.getRoot()));
		} catch (IOException | BadDataAnnotationException
				| ParserConfigurationException | SAXException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bdIn != null) {
					bdIn.close();
				}
				if (wr != null){
					wr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;

	}
}
