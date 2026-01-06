/**
 * 
 */
/**
 * @author ZribaY
 *
 */
package fr.fms.trainingstore.app;


import fr.fms.trainingstore.config.ConnectionFactory;

import java.sql.Connection;

public class DbConnectionTest {

    public static void main(String[] args) {
        try (Connection cn = ConnectionFactory.getInstance().getConnection()) {
            System.out.println("Connexion OK : " + !cn.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
