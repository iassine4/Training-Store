/**
 * 
 */
package fr.fms.trainingstore.dao;

/**
 * @author ZribaY
 *
 */
import fr.fms.trainingstore.domain.Modality;
import fr.fms.trainingstore.domain.Training;

import java.util.List;

public interface TrainingDao extends Dao<Training> {
	
	List<Training> findByCategoryId(int categoryId);
    List<Training> findByModality(Modality modality);
    List<Training> searchByKeyword(String keyword);

}
