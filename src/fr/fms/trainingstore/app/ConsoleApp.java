/**
 * 
 */
package fr.fms.trainingstore.app;

import java.util.List;
import java.util.Scanner;

import fr.fms.trainingstore.dao.jdbc.CategoryDaoJdbc;
import fr.fms.trainingstore.dao.jdbc.TrainingDaoJdbc;
import fr.fms.trainingstore.domain.Category;
import fr.fms.trainingstore.domain.Modality;
import fr.fms.trainingstore.domain.Training;
import fr.fms.trainingstore.service.CatalogService;

/**
 * @author ZribaY
 *
 */
public class ConsoleApp {

	/**
	 * @param args
	 */
	 private final Scanner scanner = new Scanner(System.in);
	 
	 //CatalogService contient la logique métier : lister des trainings, rechercher, récupérer une catégorie, etc
	    private final CatalogService catalogService =
	            new CatalogService(new CategoryDaoJdbc(), new TrainingDaoJdbc());//Les DAO 2dependances(CategoryDaoJdbc, TrainingDaoJdbc) contiennent l’accès BDD (SQL, PreparedStatement, ResultSet)
	    
	public static void main(String[] args) {
		
		new ConsoleApp().run();

	}

	public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Votre choix : ");
            if (choice < 0) {
                System.out.println("Veuillez saisir un choix positif.");
                continue;
            }
            switch (choice) {
                case 1 -> displayTrainings(catalogService.listTrainings());
                case 2 -> handleFilterByCategory();
                case 3 -> handleFilterByModality();
                case 4 -> handleSearch();
                case 5 -> handleTrainingDetail();
                case 0 -> running = false;
                default -> System.out.println("Choix invalide.");
            }
        }

        System.out.println("Au revoir.");
    }

    private void printMenu() {
        System.out.println("\n=== TRAINING STORE / CATALOGUE ===");
        System.out.println("1 - Afficher toutes les formations");
        System.out.println("2 - Filtrer par catégorie");
        System.out.println("3 - Filtrer par modalité (ONSITE/REMOTE)");
        System.out.println("4 - Rechercher par mot-clé");
        System.out.println("5 - Consulter le détail d'une formation");
        System.out.println("0 - Quitter");
    }

    private void handleFilterByCategory() {
        List<Category> categories = catalogService.listCategories();
        System.out.println("\n=== CATEGORIES ===");
        categories.forEach(System.out::println);

        int categoryId = readInt("Id catégorie : ");
        displayTrainings(catalogService.listByCategory(categoryId));
    }

    private void handleFilterByModality() {
        System.out.println("\n1 - ONSITE");
        System.out.println("2 - REMOTE");
        int choice = readInt("Choix modalité : ");

        Modality modality = (choice == 1) ? Modality.ONSITE : Modality.REMOTE;
        displayTrainings(catalogService.listByModality(modality));
    }

    private void handleSearch() {
        System.out.print("Mot-clé : ");
        String keyword = scanner.nextLine();
        displayTrainings(catalogService.search(keyword));
    }

    private void handleTrainingDetail() {
        int trainingId = readInt("Id formation : ");
        Training training = catalogService.getTraining(trainingId);

        if (training == null) {
            System.out.println("Formation introuvable.");
            return;
        }

        System.out.println("\n=== DETAIL FORMATION ===");
        System.out.println("Id         : " + training.getId());
        System.out.println("Nom        : " + training.getName());
        System.out.println("Description: " + training.getDescription());
        System.out.println("Durée (j)  : " + training.getDurationDays());
        System.out.println("Modalité   : " + training.getModality());
        System.out.println("Prix       : " + training.getPrice() + " €");
        System.out.println("Catégorie  : " + (training.getCategory() == null ? "none" : training.getCategory().getLabel()));
    }

    private void displayTrainings(List<Training> trainings) {
        System.out.println("\n=== FORMATIONS ===");
        if (trainings == null || trainings.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        trainings.forEach(System.out::println);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
            	//convertit la chaîne en int
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir un nombre.");
            }
        }
    }
}
