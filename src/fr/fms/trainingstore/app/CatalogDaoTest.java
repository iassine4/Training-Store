package fr.fms.trainingstore.app;

import fr.fms.trainingstore.dao.CategoryDao;
import fr.fms.trainingstore.dao.TrainingDao;
import fr.fms.trainingstore.dao.jdbc.CategoryDaoJdbc;
import fr.fms.trainingstore.dao.jdbc.TrainingDaoJdbc;
import fr.fms.trainingstore.domain.Modality;

public class CatalogDaoTest {

	public static void main(String[] args) {

		 CategoryDao categoryDao = new CategoryDaoJdbc();
	        TrainingDao trainingDao = new TrainingDaoJdbc();

	        System.out.println("=== CATEGORIES ===");
	        categoryDao.findAll().forEach(System.out::println);

	        System.out.println("\n=== TRAININGS (ALL) ===");
	        trainingDao.findAll().forEach(System.out::println);

	        System.out.println("\n=== TRAININGS (REMOTE) ===");
	        trainingDao.findByModality(Modality.REMOTE).forEach(System.out::println);

	        System.out.println("\n=== TRAININGS (keyword='java') ===");
	        trainingDao.searchByKeyword("sq").forEach(System.out::println);

	}

}
