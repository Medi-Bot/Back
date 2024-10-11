package com.medibot.back.sqlite;

import com.medibot.back.entities.*;
import com.medibot.back.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class SqliteActions {
    @Autowired
    public AntecedentRepository antecedentRepository;
    @Autowired
    public HistoriqueCommunicationRepository historiqueCommunicationRepository;
    @Autowired
    public InformationsRepository informationsRepository;
    @Autowired
    public PoidsRepository poidsRepository;
    @Autowired
    public TailleRepository tailleRepository;
    @Autowired
    public MedicamentUtiliseRepository medicamentUtiliseRepository;

    public static String CurrentBase;
    protected static String CurrentPassword;

    /**
     * Return true if the base exist
     * @param name
     * @return
     */
    public static boolean Exist(String name){
        File file = new File (name + ".db");
        return file.exists();
    }

    public static boolean Exist(){
        return Exist(CurrentBase);
    }

    public static void SetPassword(String newPassword){
        CurrentPassword = newPassword;
    }

    public static boolean CorrectPassword(String name, String password){
        try {
            Connection conn = OpenBase();
            CloseBase(conn);
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }

    public static boolean CorrectPassword(){
        return CorrectPassword(CurrentBase, CurrentPassword);
    }

    public static List<String> AllExistingBases(){
        File dir = new File(".");
        return Arrays.stream(Objects.requireNonNullElse(dir.listFiles((d, name) -> name.endsWith(".db")), new File[]{})).map(file -> file.getName().replace(".db", "")).toList();
    }

    /**
     * Decrypt the base and return the connection to the base
     * @param name
     * @return
     * @throws SQLException
     */
    public static Connection OpenBase(String name) throws SQLException {
        // TO DO : Decrypt the base
        return DriverManager.getConnection("jdbc:sqlite:" + name + ".db", "", CurrentPassword);
    }

    public static Connection OpenBase() throws SQLException {
        return OpenBase(CurrentBase);
    }

    /**
     * Close the base and encrypt it
     * @param conn
     * @param name
     * @throws SQLException
     */
    public static void CloseBase(Connection conn, String name) throws SQLException {
        conn.close();
        // TO DO : encrypt the base
    }

    public static void CloseBase(Connection conn) throws SQLException{
        CloseBase(conn, CurrentBase);
    }

    /**
     * Init the base with the standard schema.
     * WARNING : If the base already exist, it's overridden
     */
    public static void InitBase(String name){
        Connection conn = null;
        try {
            conn = OpenBase(name);
            try {
                ClassPathResource classPathResource = new ClassPathResource("schema.sql");
                InputStream in = classPathResource.getInputStream();
                Statement statement = conn.createStatement();
                String query = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                String[] queries = query.split(";");
                Arrays.stream(queries).forEach(elt -> {
                    try {
                        statement.executeUpdate(elt);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                statement.close();
            } catch (IOException e) {
                System.out.println("ERREUR : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    CloseBase(conn, name);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void InitBase()  {
        InitBase(CurrentBase);
    }

    /**
     * Moc base values
     */
    public static void MockBaseValue(String name){
        Connection conn = null;
        try {
            conn = OpenBase(name);
            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into Antecedent(date, description) values ('date1', 'desc1'),('date2', 'desc2'),('date3', 'desc3');");
            statement.executeUpdate("insert into HistoriqueCommunication(date, message, reponse) values ('date1', 'mess1', 'rep1'), ('date2', 'mess2', 'rep2'), ('date3', 'mess3', 'rep3'); ");
            statement.executeUpdate("insert into Informations(id, date_de_naissance) values (1, 'date1');");
            statement.executeUpdate("insert into MedicamentUtilise(date_de_debut, nom, frequence, date_de_fin) VALUES ('dateD1', 'nom1', 'freq1', 'dateF1'), ('dateD2', 'nom2', 'freq2', 'dateF2'), ('dateD3', 'nom3', 'freq3', 'dateF3');");
            statement.executeUpdate("insert into Poids(date, poids) VALUES ('date1', 'poids1'), ('date2', 'poids2'), ('date3', 'poids3');");
            statement.executeUpdate("insert into Taille(date, taille) VALUES ('date1', 'taille1'), ('date2', 'taille2'), ('date3', 'taille3');");
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    CloseBase(conn, name);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void MockBaseValue() {
        MockBaseValue(CurrentBase);
    }

    /**
     * Import the base to H2.
     * WARNING : Will clear the current value in the H2 base
     */
    public void ImportBase(String name){
        Connection conn = null;
        try {
            conn = OpenBase(name);
            Statement statement = conn.createStatement();
            ImportAntecedents(statement);
            ImportHistoriqueCommunication(statement);
            ImportInformations(statement);
            ImportPoids(statement);
            ImportTailles(statement);
            ImportMedicamentUtilises(statement);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    CloseBase(conn, name);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void ImportBase() {
        ImportBase(CurrentBase);
    }

    protected void ImportAntecedents(Statement statement) throws SQLException{
        List<Antecedent> antecedents = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from Antecedent;");
        while (res.next()){
            antecedents.add(new Antecedent(res.getString("date"), res.getString("description")));
        }
        antecedentRepository.saveAll(antecedents);
    }

    protected void ImportHistoriqueCommunication(Statement statement) throws SQLException{
        List<HistoriqueCommunication> historiqueCommunications = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from HistoriqueCommunication;");
        while (res.next()){
            historiqueCommunications.add(new HistoriqueCommunication(res.getString("date"), res.getString("message"), res.getString("reponse")));
        }
        historiqueCommunicationRepository.saveAll(historiqueCommunications);
    }

    protected void ImportInformations(Statement statement) throws SQLException{
        List<Informations> informationsList = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from Informations;");
        while (res.next()){
            informationsList.add(new Informations(res.getInt("id"), res.getString("date_de_naissance")));
        }
        informationsRepository.saveAll(informationsList);
    }

    protected void ImportPoids(Statement statement) throws SQLException{
        List<Poids> poidsList = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from Poids;");
        while (res.next()){
            poidsList.add(new Poids(res.getString("date"), res.getFloat("poids")));
        }
        poidsRepository.saveAll(poidsList);
    }

    protected void ImportTailles(Statement statement) throws SQLException{
        List<Taille> tailles = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from Taille;");
        while (res.next()){
            tailles.add(new Taille(res.getString("date"), res.getFloat("taille")));
        }
        tailleRepository.saveAll(tailles);
    }

    protected void ImportMedicamentUtilises(Statement statement) throws SQLException{
        List<MedicamentUtilise> medicamentUtilises = new ArrayList<>();
        ResultSet res = statement.executeQuery("select * from MedicamentUtilise;");
        while (res.next()){
            medicamentUtilises.add(new MedicamentUtilise(new MedicamentUtilise.MedicamentUtiliseId(res.getString("date_de_debut"), res.getString("nom")),res.getString("frequence"), res.getString("date_de_fin")));
        }
        medicamentUtiliseRepository.saveAll(medicamentUtilises);
    }

    /**
     * Export the H2 to the base
     * WARNING : will overwrite the base if it exists
     */
    public void ExportBase(String name){
        InitBase(name);
        Connection conn = null;
        try {
            conn = OpenBase(name);
            Statement statement = conn.createStatement();
            ExportAntecedents(statement);
            ExportHistoriqueCommunications(statement);
            ExportInformations(statement);
            ExportMedicamentUtilises(statement);
            ExportPoids(statement);
            ExportTailles(statement);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    CloseBase(conn, name);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void ExportBase() {
        ExportBase(CurrentBase);
    }

    protected void ExportAntecedents(Statement statement) throws SQLException{
        List<Antecedent> antecedents = antecedentRepository.findAll();
        String query = "insert into Antecedent(date, description) values ";
        for (Antecedent antecedent : antecedents) {
            statement.executeUpdate(query + antecedent.toExport());
        }
    }

    protected void ExportHistoriqueCommunications(Statement statement) throws SQLException{
        List<HistoriqueCommunication> historiqueCommunications = historiqueCommunicationRepository.findAll();
        String query = "insert into HistoriqueCommunication(date, message, reponse) values ";
        for (HistoriqueCommunication historiqueCommunication : historiqueCommunications) {
            statement.executeUpdate(query + historiqueCommunication.toExport());
        }
    }

    protected void ExportInformations(Statement statement) throws SQLException{
        List<Informations> informationsList = informationsRepository.findAll();
        String query = "insert into Informations(id, date_de_naissance) values ";
        for (Informations informations : informationsList) {
            statement.executeUpdate(query + informations.toExport());
        }
    }

    protected void ExportMedicamentUtilises(Statement statement) throws SQLException{
        List<MedicamentUtilise> medicamentUtilises = medicamentUtiliseRepository.findAll();
        String query = "insert into MedicamentUtilise(date_de_debut, nom, frequence, date_de_fin) values ";
        for (MedicamentUtilise medicamentUtilise : medicamentUtilises) {
            statement.executeUpdate(query + medicamentUtilise.toExport());
        }
    }

    protected void ExportPoids(Statement statement) throws SQLException{
        List<Poids> poidsList = poidsRepository.findAll();
        String query = "insert into Poids(date, poids) values ";
        for (Poids poids : poidsList) {
            statement.executeUpdate(query + poids.toExport());
        }
    }

    protected void ExportTailles(Statement statement) throws SQLException{
        List<Taille> tailles = tailleRepository.findAll();
        String query = "insert into Taille(date, taille) values ";
        for (Taille taille : tailles) {
            statement.executeUpdate(query + taille.toExport());
        }
    }

    /**
     * Clear everything inside the H2 base.
     * Always call ExportBase before.
     */
    public void ClearH2(){
        antecedentRepository.deleteAll();
        historiqueCommunicationRepository.deleteAll();
        informationsRepository.deleteAll();
        poidsRepository.deleteAll();
        tailleRepository.deleteAll();
        medicamentUtiliseRepository.deleteAll();
    }

    // TO DO : Faire que la sauvegarde se fait apres chaque appelle a une methode d'un service ?? (attention au boucle infini => ne pas utiliser les services depuis cette classe )
}
