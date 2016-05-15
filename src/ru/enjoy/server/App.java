package ru.enjoy.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.google.gson.*;

import ru.enjoy.server.data.Root;
import ru.enjoy.server.exceptions.BadDataAnnotationException;
import ru.enjoy.server.objbuilder.ObjectBuilder;
public class App {

	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BadDataAnnotationException {
		if (args.length < 2) {
			System.out.println("Error! В строке запуска EnjoyDB2json, должны быть "
					+ "указаны 2 параметра: путь файла выгруженной базы и путь к "
					+ "файлу в который записать json данные");
			return;
		}
		ObjectBuilder ob = new ObjectBuilder(new Root());
		DataBase2Object loader = new DataBase2Object();
		loader.load(new FileInputStream(args[0]), ob);
		ob.makeStruct();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(ob.getRoot()));
		return;

	}

}
