package projekt_pc2t;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDatabase {
    private Map<Integer, Student> studenti;
    private int dalsiId;

    public StudentDatabase() {
        studenti = new HashMap<>();
        dalsiId = 1;
        vytvorTabulkuStudenti();
        nactiStudentyZDB();
    }

    public void pridejStudenta(Student student) {
        studenti.put(student.getId(), student);
        dalsiId++;
    }

    public Student najdiStudenta(int id) {
        return studenti.get(id);
    }

    public void odeberStudenta(int id) {
        if (studenti.containsKey(id)) {
            studenti.remove(id);
            odeberStudentaZDB(id); 
            System.out.println("Student s ID " + id + " byl odstraněn.");
        } else {
            System.out.println("Student s ID " + id + " nebyl nalezen.");
        }
    }
    
    private void odeberStudentaZDB(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseInit.connect();
            String sql = "DELETE FROM studenti WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student s ID " + id + " byl odstraněn z databáze.");
            } else {
                System.out.println("Student s ID " + id + " nebyl nalezen v databázi.");
            }
        } catch (SQLException e) {
            System.err.println("Chyba při odstraňování studenta z databáze: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání Connection: " + e.getMessage());
                }
            }
        }
    }

    public int getDalsiId() {
        return dalsiId;
    }
    
    public void vypisStudentyPodlePrijmeni() {
        List<Student> telekomunikace = new ArrayList<>();
        List<Student> kyberbezpecnost = new ArrayList<>();

        for (Student student : studenti.values()) {
            if (student instanceof StudentTele) {
                telekomunikace.add(student);
            } else if (student instanceof StudentKyb) {
                kyberbezpecnost.add(student);
            }
        }

        telekomunikace.sort((s1, s2) -> s1.getPrijmeni().compareToIgnoreCase(s2.getPrijmeni()));
        kyberbezpecnost.sort((s1, s2) -> s1.getPrijmeni().compareToIgnoreCase(s2.getPrijmeni()));

        System.out.println("\n--- Studenti Telekomunikace ---");
        for (Student student : telekomunikace) {
            System.out.printf("ID: %d, Jméno: %s, Příjmení: %s, Rok narození: %d, Průměr: %.2f\n",
                    student.getId(),
                    student.getJmeno(),
                    student.getPrijmeni(),
                    student.getRokNarozeni(),
                    student.getStudijniPrumer());
        }

        System.out.println("\n--- Studenti Kyberbezpečnost ---");
        for (Student student : kyberbezpecnost) {
            System.out.printf("ID: %d, Jméno: %s, Příjmení: %s, Rok narození: %d, Průměr: %.2f\n",
                    student.getId(),
                    student.getJmeno(),
                    student.getPrijmeni(),
                    student.getRokNarozeni(),
                    student.getStudijniPrumer());
        }
    }

    
    public void vypocitejStudijniPrumery() {
        double soucetTele = 0;
        int pocetTele = 0;
        double soucetKyb = 0;
        int pocetKyb = 0;

        for (Student student : studenti.values()) {
            if (student instanceof StudentTele) {
                soucetTele += student.getStudijniPrumer();
                pocetTele++;
            } else if (student instanceof StudentKyb) {
                soucetKyb += student.getStudijniPrumer();
                pocetKyb++;
            }
        }

        if (pocetTele > 0) {
            System.out.printf("Průměr známek studentů Telekomunikací: %.2f\n", soucetTele / pocetTele);
        } else {
            System.out.println("Žádní studenti Telekomunikací.");
        }

        if (pocetKyb > 0) {
            System.out.printf("Průměr známek studentů Kyberbezpečnosti: %.2f\n", soucetKyb / pocetKyb);
        } else {
            System.out.println("Žádní studenti Kyberbezpečnosti.");
        }
    }
    
    public void vypisPocetStudentuVeSkupinach() {
        int pocetTele = 0;
        int pocetKyb = 0;

        for (Student student : studenti.values()) {
            if (student instanceof StudentTele) {
                pocetTele++;
            } else if (student instanceof StudentKyb) {
                pocetKyb++;
            }
        }

        System.out.println("Počet studentů Telekomunikací: " + pocetTele);
        System.out.println("Počet studentů Kyberbezpečnosti: " + pocetKyb);
    }
    
    public void ulozStudentaDoSouboru(int id) {
        Student student = studenti.get(id);
        if (student != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("student_" + id + ".txt"))) {
                String radek = (student instanceof StudentTele ? "StudentTele" : "StudentKyb") + ";" +
                        student.id + ";" +
                        student.jmeno + ";" +
                        student.prijmeni + ";" +
                        student.rokNarozeni + ";";

                String znamky = "";
                for (int i = 0; i < student.znamky.size(); i++) {
                    znamky += student.znamky.get(i);
                    if (i < student.znamky.size() - 1) {
                        znamky += ",";
                    }
                }
                radek += znamky + ";";
                radek += String.format("%.2f", student.getStudijniPrumer()) + ";";
                radek += (student instanceof StudentTele ? "Telekomunikace" : "Kyberbezpečnost");
                writer.write(radek);

                System.out.println("Student úspěšně uložen do souboru student_" + id + ".txt");
            } catch (IOException e) {
                System.out.println("Chyba při ukládání studenta: " + e.getMessage());
            }
        } else {
            System.out.println("Student s ID " + id + " nebyl nalezen.");
        }
    }
    
    public void nactiStudentaZeSouboru(String nazevSouboru) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nazevSouboru))) {
            String radek = reader.readLine();
            if (radek != null) {
                String[] casti = radek.split(";");

                String typStudenta = casti[0];
                int id = Integer.parseInt(casti[1]);
                String jmeno = casti[2];
                String prijmeni = casti[3];
                int rokNarozeni = Integer.parseInt(casti[4]);
                
                String[] znamkyText = casti[5].split(",");
                List<Integer> znamky = new ArrayList<>();
                for (String znamka : znamkyText) {
                    znamky.add(Integer.parseInt(znamka.trim()));
                }

                double studijniPrumer = Double.parseDouble(casti[6].replace(',', '.'));
                String obor = casti[7];

                Student student;
                if (typStudenta.equals("StudentTele")) {
                    student = new StudentTele(id, jmeno, prijmeni, rokNarozeni);
                } else {
                    student = new StudentKyb(id, jmeno, prijmeni, rokNarozeni);
                }

                student.setZnamky(znamky);
                student.setStudijniPrumer(studijniPrumer);
                student.setObor(obor);

                studenti.put(id, student);

                System.out.println("Student úspěšně načten ze souboru: " + nazevSouboru);
            } else {
                System.out.println("Soubor " + nazevSouboru + " je prázdný.");
            }
        } catch (IOException e) {
            System.out.println("Chyba při načítání studenta: " + e.getMessage());
        }
    }
    
    private void vytvorTabulkuStudenti() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DatabaseInit.connect(); 
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS studenti (" +
                    "id INTEGER PRIMARY KEY, " + 
                    "skupina TEXT NOT NULL, "
                    + "jmeno TEXT NOT NULL, "
                    + "prijmeni TEXT NOT NULL, "
                    + "rok_narozeni INTEGER NOT NULL, "
                    + "znamky TEXT, "
                    + "obor TEXT" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("Tabulka 'studenti' úspěšně vytvořena (pokud neexistovala).");
        } catch (SQLException e) {
            System.err.println("Chyba při vytváření tabulky: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání statementu: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close(); 
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání připojení k databázi: " + e.getMessage());
                }
            }
        }
    }
    
    public void ulozVsechnyStudentyDoDB() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseInit.connect();
            conn.setAutoCommit(false);
            String sql = "INSERT OR REPLACE INTO studenti (id, skupina, jmeno, prijmeni, rok_narozeni, znamky, obor) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            System.out.println("Počet studentů k uložení: " + studenti.size());

            for (Student student : studenti.values()) {
                pstmt.setInt(1, student.getId());
                pstmt.setString(2, student instanceof StudentTele ? "Telekomunikace" : "Kyberbezpečnost");
                pstmt.setString(3, student.getJmeno());
                pstmt.setString(4, student.getPrijmeni());
                pstmt.setInt(5, student.getRokNarozeni());
                String znamky = String.join(",", student.znamky.stream().map(String::valueOf).toArray(String[]::new));
                pstmt.setString(6, znamky);
                pstmt.setString(7, student.getClass().getSimpleName().substring(7));
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();    

            System.out.println("Všechny informace o studentech úspěšně zapsány do databáze.");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transakce vrácena kvůli chybě.");
                } catch (SQLException ex) {
                    System.err.println("Chyba při rollbacku transakce: " + ex.getMessage());
                }
            }
            System.err.println("Chyba při ukládání/aktualizaci všech studentů do databáze: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání Connection: " + e.getMessage());
                }
            }
        }
    }
    
    public void nactiStudentyZDB() {
        vytvorTabulkuStudenti(); 
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseInit.connect();
            stmt = conn.createStatement();
            String sql = "SELECT id, skupina, jmeno, prijmeni, rok_narozeni, znamky, obor FROM studenti";
            rs = stmt.executeQuery(sql);
            studenti.clear(); 
            dalsiId = 1; 

            while (rs.next()) {
                int id = rs.getInt("id");
                String skupina = rs.getString("skupina");
                String jmeno = rs.getString("jmeno");
                String prijmeni = rs.getString("prijmeni");
                int rokNarozeni = rs.getInt("rok_narozeni");
                String znamkyText = rs.getString("znamky");
                String obor = rs.getString("obor");

                List<Integer> znamky = new ArrayList<>();
                if (znamkyText != null && !znamkyText.isEmpty()) {
                    String[] znamkyArray = znamkyText.split(",");
                    for (String znamka : znamkyArray) {
                        znamky.add(Integer.parseInt(znamka.trim()));
                    }
                }

                Student student = null;
                if (skupina.equals("Telekomunikace")) {
                    student = new StudentTele(id, jmeno, prijmeni, rokNarozeni);
                } else if (skupina.equals("Kyberbezpečnost")) {
                    student = new StudentKyb(id, jmeno, prijmeni, rokNarozeni);
                }

                if (student != null) {
                    student.setZnamky(znamky);
                    student.setObor(obor); 
                    studenti.put(id, student);
                    dalsiId = Math.max(dalsiId, id + 1);
                }
            }
            System.out.println("Studenti úspěšně načteni z databáze.");
        } catch (SQLException e) {
            System.err.println("Chyba při načítání studentů z databáze: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání ResultSet: " + e.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání Statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Chyba při zavírání Connection: " + e.getMessage());
                }
            }
        }
    }
}
