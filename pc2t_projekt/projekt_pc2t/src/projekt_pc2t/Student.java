package projekt_pc2t;

import java.util.ArrayList;
import java.util.List;

public abstract class Student {
    protected int id;
    protected String jmeno;
    protected String prijmeni;
    protected int rokNarozeni;
    protected List<Integer> znamky;
    protected double studijniPrumer;
    protected String obor;

    public Student(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
        this.znamky = new ArrayList<>();
        this.studijniPrumer = 0.0;
        this.obor = "";
    }

    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public int getRokNarozeni() {
        return rokNarozeni;
    }
    
    public List<Integer> getZnamky() {
        return znamky;
    }

    public void pridejZnamku(int znamka) {
        if (znamka >= 1 && znamka <= 5) {
            znamky.add(znamka);
        } else {
            throw new IllegalArgumentException("Známka musí být mezi 1 a 5.");
        }
    }

    public double getStudijniPrumer() {
        if (znamky.isEmpty()) return 0.0;
        int suma = 0;
        for (int z : znamky) {
            suma += z;
        }
        return (double) suma / znamky.size();
    }

    public abstract void spustDovednost();
    
    public void setZnamky(List<Integer> znamky) {
        this.znamky = znamky;
    }

    public void setStudijniPrumer(double studijniPrumer) {
        this.studijniPrumer = studijniPrumer;
    }

    public void setObor(String obor) {
        this.obor = obor;
    }

}