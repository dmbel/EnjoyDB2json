package ru.enjoy.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.data.ObjectDataBase;
import ru.enjoy.server.data.Product;

public class App {

	public static void main(String[] args) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (args.length < 2) {
			System.out.println("Error! В строке запуска EnjoyDB2json, должны быть "
					+ "указаны 2 параметра: путь файла выгруженной базы и путь к "
					+ "файлу в который записать json данные");
			return;
		}

		DataBase2Object loader = new DataBase2Object(args[0]);

		ObjectDataBase odb = null;
		try {
			odb = loader.load();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Product p = new Product();
		p.id = 1;
		p.name = "qwerty";
		p.type = 4;
		p.comment = "simple comm\nddd";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(odb));
	}

}
