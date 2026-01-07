/**
 * 
 */
package fr.fms.trainingstore.domain;

/**
 * @author ZribaY
 *
 */
public class CartLine {
	
	private Training training;
    private int quantity;
    
    
    public CartLine() {
    	
    }
    
    public CartLine(Training training, int quantity) {
        this.training = training;
        this.quantity = quantity;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return training + " | qty=" + quantity;
    }

}
