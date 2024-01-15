import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;



public class Query_8 extends JFrame {
        private Connection  connection;

    public Query_8(JFrame parent, Connection connection) {
        this.connection = connection;
        try {
            // Query per selezionare tutte le scuderie
            String query = "SELECT S.NomeScuderia, SUM(PA.QuotaFinanziaria) AS SommaFinanziamenti\n" +
                    "FROM PilotaAM PA\n" +
                    "JOIN Equipaggio E ON PA.Equipaggio = E.IDEquipaggio\n" +
                    "JOIN Scuderia S ON E.Scuderia = S.NomeScuderia\n" +
                    "WHERE PA.QuotaFinanziaria IS NOT NULL\n" +
                    "GROUP BY S.NomeScuderia";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Eseguire la query e ottenere il risultato
            ResultSet resultSet = preparedStatement.executeQuery();

            // Creare una finestra per visualizzare la tabella
            JFrame tabellaScuderieFrame = new JFrame("Tabella");
            tabellaScuderieFrame.setSize(500, 300);
            tabellaScuderieFrame.setLocationRelativeTo(this);
            tabellaScuderieFrame.setLayout(new BorderLayout());

            // Creare un JTable per visualizzare i dati della tabella
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);

            // Aggiungere il JTable al frame
            tabellaScuderieFrame.add(scrollPane, BorderLayout.CENTER);

            // Mostrare la finestra
            tabellaScuderieFrame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante la visualizzazione della tabella", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo di utilità per costruire un TableModel da un ResultSet
    private DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Nome delle colonne
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Dati della tabella
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(resultSet.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }
}



