package API;

import model.Topic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static JDBCImplement jdbc;
    private static Scanner scanner;
    private Connection getConnection() {
        return null;
    };
    public static void main(String[] args) {
//        String url = "jdbc:postgresql://localhost:5432/postgres";
//        String names = "postgres";
//        String password = "12345";

//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setUser("postgres");
//        dataSource.setPassword("12345");
//        dataSource.setDatabaseName("postgres");

        JDBCImplement jdbcImplement = new JDBCImplement();
        try(Connection conn = jdbcImplement.dataSource().getConnection()){
//       try(Connection conn = DriverManager.getConnection(url,names,password)) {
           System.out.println(conn.getSchema());
           // 1. create SQL statement object
           String selectSql = "SELECT * FROM topices";
           PreparedStatement statement = conn.prepareStatement(selectSql);
           //2. Execute SQL Statement object
           ResultSet resultSet = statement.executeQuery();
           //3. Process Result with ResultSet
           List<Topic> topics = new ArrayList<>();
           while (resultSet.next()){
               Integer id = resultSet.getInt("id");
               String name = resultSet.getString("name");
               String description = resultSet.getString("description");
               Boolean status = resultSet.getBoolean("status");
               topics.add(new Topic(id, name, description, status));


               jdbc = new JDBCImplement();
               scanner = new Scanner(System.in);

               Topic topic = new Topic();
               System.out.println("Enter Name :");
               topic.setName(scanner.nextLine());

               System.out.println("Enter Description :");
               topic.setDescription(scanner.nextLine());
               topic.setStatus(true);

               insertTopic(topic);
//             insertSelect(topic);
           }
           topics.forEach(System.out::println);
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }
    static void fetchStudentData(Connection connection, API.Main main){
        String query = "SELECT * FROM topices ORDER BY id";

        List<Topic> topicList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){

                Topic top = new Topic();
                top.setId(resultSet.getInt("id"));
                top.setName(resultSet.getString("name"));

                // add student object to student's list
                topicList.add(top);
            }
//            main.displayStudents(topicList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void insertTopic (Topic topic){

        try (Connection conn = jdbc.dataSource().getConnection()){
            String insertSql = "INSERT INTO topices (name, description, status) " +
                    "VALUES(?, ?, ?) ";
            PreparedStatement statement = conn.prepareStatement(insertSql);
            statement.setString(1,topic.getName());
            statement.setString(2,topic.getDescription());
            statement.setBoolean(3,topic.getStatus());

            int count = statement.executeUpdate();
            System.out.println(count);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Topic selectStudentById(Connection connection, int id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM topices WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Topic topic = new Topic();
                topic.setId(resultSet.getInt("id"));
                topic.setName(resultSet.getString("name"));
                topic.setDescription(resultSet.getString("description"));
                return topic;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Topic> selectStudentsByName(Connection connection, String name) {
        List<Topic> topicList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM topices WHERE name like ?")) {
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Topic topic = new Topic();
                topic.setId(resultSet.getInt("id"));
                topic.setName(resultSet.getString("name"));
                topic.setDescription(resultSet.getString("description"));
                topicList.add(topic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topicList;
    }



    void updateStudent(Connection connection, API.Main main) {
        Scanner scanner = new Scanner(System.in);
        try {
            Statement statement = connection.createStatement();
            System.out.println("Enter the ID of the student you want to update:");
            int id = scanner.nextInt();
            scanner.nextLine();
            ResultSet rs = statement.executeQuery("SELECT * FROM topices WHERE id = " + id);

            // Check if the student ID exists
            if (!rs.next()) {
                System.out.println("Student ID not found.");
                return;
            }

            System.out.println("Enter the new name for the student:");
            String name = scanner.nextLine();
            PreparedStatement ps = connection.prepareStatement("UPDATE topices SET name = ? WHERE id =?");
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Update successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    void deleteStudent(Connection connection, API.Main main){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter student ID to delete:");
            int idToDelete = scanner.nextInt();

            // Check if the student exists
            String query = "SELECT * FROM topices WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idToDelete);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No student found with ID " + idToDelete);
                return;
            }

            // Delete the student
            PreparedStatement ps = connection.prepareStatement("DELETE FROM topices WHERE id = ?");
            ps.setInt(1, idToDelete);
            ps.executeUpdate();

            System.out.println("Deleted student with ID " + idToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

{
        // input Scanner for input
        Scanner scanner = new Scanner(System.in);
        Topic topic = new Topic();

        // call all function
        API.Main main = new Main();
        Connection connection = main.getConnection();



        int choice;
        do  {
            System.out.println("Please Choose option");
            System.out.println("1. Select all record");
            System.out.println("2. Select record by ID");
            System.out.println("3. Select record by Name");
            System.out.println("4. Insert a Record");
            System.out.println("5. Update Record by ID");
            System.out.println("6. Deleted a record by ID");
            System.out.println("7. Exit");
            System.out.print("Please choice option: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    fetchStudentData(connection, main);


                    break;


                case 2:
                    System.out.println("Pleas Input Id: ");
                    int id = scanner.nextInt(); // the ID of the record to select
                    Topic topic1 = main.selectStudentById(connection, id);
                    if (topic != null) {
                        // display the selected student
                        System.out.printf("ID: %d, Name: %s\n",
                                topic.getId(), topic.getName());
                    } else {
                        System.out.println("No student found with ID " + id);
                    }
                    break;
                case 3:
                    System.out.println("Pleas Input Name: ");
                    String name = scanner.next();
                    List<Topic> studentList = main.selectStudentsByName(connection, name);


                    for (Topic topic3 : studentList) {
                        System.out.println(topic3.getId() + " " + topic3.getName() + " " );
                    }
                    break;


                case 4:

                    System.out.println("Enter name : ");
                    topic.setName(scanner.next());
                    System.out.println("Enter Age : ");
                    main.insertTopic(topic);
                    break;




                case 5:

                    main.updateStudent(connection, main);
                    break;

                case 6:
                    main.deleteStudent(connection,main);
                    break;

                case 7:
                    System.out.println("exit");
                    break;
            }
        }while (choice!=7);



    }




}
