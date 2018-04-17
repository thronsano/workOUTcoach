package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Client;
import com.workOUTcoach.utility.DatabaseConnector;
import com.workOUTcoach.utility.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ClientModel {
    private static Map<Integer, Client> clients;

    public Collection<Client> getAllClients() {
        clients = loadClients();

        return this.clients.values();
    }

    private static HashMap<Integer, Client> loadClients() {
        HashMap<Integer, Client> clientsMap = new HashMap<>();

        try {
            ResultSet resultSet = DatabaseConnector.executeQuery("SELECT * FROM CLIENTS");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                Client client = extractClient(resultSet, resultSetMetaData);
                clientsMap.put(client.getId(), client);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return clientsMap;
    }

    private static Client extractClient(ResultSet resultSet, ResultSetMetaData resultSetMetaData) throws SQLException {
        //Client's variables
        int id = 0;
        String name = null;
        String email = null;

        int numberOfColumns = resultSetMetaData.getColumnCount();

        //LOAD CLIENT'S DATA
        for (int i = 1; i <= numberOfColumns; i++) {
            Object result = resultSet.getObject(i);

            if (result != null) {
                switch (resultSetMetaData.getColumnName(i)) {
                    case "name":
                        name = result.toString();
                        break;
                    case "email":
                        email = result.toString();
                        break;
                    case "id":
                        id = ((Number) result).intValue();
                        break;
                    default:
                        Logger.logWarning("Unknown client's field: " + resultSetMetaData.getColumnName(i));
                        break;
                }
            }
        }

        return new Client(id, name, email);
    }
}