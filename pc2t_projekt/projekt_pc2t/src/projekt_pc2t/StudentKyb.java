package projekt_pc2t;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StudentKyb extends Student {

    public StudentKyb(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni);
    }

    @Override
    public void spustDovednost() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String vstup = jmeno + prijmeni;
            byte[] hash = md.digest(vstup.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            System.out.println("Hash jména a příjmení: " + hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Chyba při hashování: " + e.getMessage());
        }
    }
}