package ru.enjoy.server;

import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.data.Root;
import ru.enjoy.server.exceptions.BadDataAnnotationException;
import ru.enjoy.server.json.JsonObjectContainer;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (args.length < 2) {
			System.out.println("Error! В строке запуска EnjoyDB2json, должны быть "
					+ "указаны 2 параметра: путь файла выгруженной базы и путь к "
					+ "файлу в который записать json данные");
			return;
		}
		DataBase2Object loader = new DataBase2Object();
		JsonObjectContainer joc;
		try {
			joc = new JsonObjectContainer(new Root());
			loader.load(new FileInputStream(args[0]), joc);
			joc.makeChilds();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(joc.root));
		} catch (ParserConfigurationException | SAXException | IOException | BadDataAnnotationException e) {
			System.out.println(e.getMessage());
			return;
		}

	}

}
