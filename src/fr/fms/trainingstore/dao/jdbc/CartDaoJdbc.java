/**
 * 
 */
package fr.fms.trainingstore.dao.jdbc;

import fr.fms.trainingstore.config.ConnectionFactory;
import fr.fms.trainingstore.dao.CartDao;
import fr.fms.trainingstore.domain.Cart;

/**
 * @author ZribaY
 *
 */
public class CartDaoJdbc implements CartDao {
	
	 private final ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

	@Override
	public Cart findOrCreateByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cart findByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addTraining(int userId, int trainingId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeOneTraining(int userId, int trainingId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clearCart(int userId) {
		// TODO Auto-generated method stub
		return false;
	}

}
