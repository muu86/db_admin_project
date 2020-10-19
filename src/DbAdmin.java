import java.util.Scanner;
import java.sql.*;

public class DbAdmin {
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    ResultSet rs = null;
    Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    String id = null;
    String pw = null;

    DbAdmin (String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    void connect(String id, String pw) {
        try {
            String dbUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
            this.id = id;
            this.pw = pw;
            con = DriverManager.getConnection(dbUrl, id, pw);
            stmt = con.createStatement();
            System.out.println("접속");
        } catch (SQLException e) {
            System.out.println("아이디를 확인하세요");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    void read(String table) {

        try {
            connect(id, pw);

            rs = stmt.executeQuery("SELECT * FROM " + table);

            while (rs.next()) {
                System.out.print(rs.getString("name"));
                System.out.print(" | ");
                System.out.print(rs.getInt("age"));
                System.out.print(" | ");
                System.out.print(rs.getInt("salary"));
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    void insert(String name, int age, int salary) {
        connect(id, pw);

        String sql = "INSERT INTO DBTEST (name, age, salary) VALUES (?, ?, ?)";

        try {
            pstmt = con.prepareStatement(sql);
            System.out.println("접속");

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, salary);

            int r = pstmt.executeUpdate();
            System.out.println("변경된 row: " + r);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    void update(String name, int age) {
        String sql = "UPDATE DBTEST SET age = ? WHERE name = ?";

        connect(id, pw);

        try{
            pstmt = con.prepareStatement(sql);
            System.out.println("접속");

            pstmt.setInt(1, age);
            pstmt.setString(2, name);

            int r = pstmt.executeUpdate();
            System.out.println("변경된 row: " + r);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    void delete(String name) {
        connect(id, pw);

        String sql = "DELETE FROM DBTEST WHERE name = ?";

        try {
            pstmt = con.prepareStatement(sql);
            System.out.println("접속");

            pstmt.setString(1, name);

            int r = pstmt.executeUpdate();
            System.out.println("변경된 row: " + r);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DbAdmin db = new DbAdmin("kmj", "1234");

        Scanner sc = new Scanner(System.in);

        String id = sc.nextLine();
        String pw = sc.nextLine();
        db.connect(id, pw);

        while (true) {
            String str = sc.nextLine();
            if (str.equals("select")) {
                db.read("DBTEST");
            } else if (str.equals("insert")) {
                String name = sc.nextLine();
                int age = sc.nextInt();
                int salary = sc.nextInt();
                db.insert(name, age, salary);
            } else if (str.equals("delete")) {
                String name = sc.nextLine();
                db.delete(name);
            } else if (str.equals("update")) {
                String name = sc.nextLine();
                int age = sc.nextInt();
                db.update(name, age);
            } else {
                if (str.equals("exit")) {
                    break;
                }
                System.out.println("올바른 명령어를 입력하세요");
            }
        }

        sc.close();
    }
}
