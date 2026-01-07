/**
 * 
 */
/**
 * @author ZribaY
 *
 */
package fr.fms.trainingstore.service;

import java.util.List;

import fr.fms.trainingstore.dao.CategoryDao;
import fr.fms.trainingstore.dao.TrainingDao;
import fr.fms.trainingstore.domain.Category;
import fr.fms.trainingstore.domain.Modality;
import fr.fms.trainingstore.domain.Training;

public class CatalogService {

    private final CategoryDao categoryDao;
    private final TrainingDao trainingDao;

    public CatalogService(CategoryDao categoryDao, TrainingDao trainingDao) {
        this.categoryDao = categoryDao;
        this.trainingDao = trainingDao;
    }

    public List<Category> listCategories() {
        return categoryDao.findAll();
    }

    public List<Training> listTrainings() {
        return trainingDao.findAll();
    }

    public Training getTraining(int trainingId) {
        return trainingDao.findById(trainingId);
    }

    public List<Training> listByCategory(int categoryId) {
        return trainingDao.findByCategoryId(categoryId);
    }

    public List<Training> listByModality(Modality modality) {
        return trainingDao.findByModality(modality);
    }

    public List<Training> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return trainingDao.findAll();
        }
        return trainingDao.searchByKeyword(keyword.trim());
    }
}