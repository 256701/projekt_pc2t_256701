package projekt_pc2t;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDatabase databaze = new StudentDatabase();

        boolean konec = false;

        while (!konec) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Přidat nového studenta");
            System.out.println("2. Přidat známku studentovi");
            System.out.println("3. Zobrazit informace o studentovi");
            System.out.println("4. Spustit dovednost studenta");
            System.out.println("5. Odstranit studenta");
            System.out.println("6. Konec");
            System.out.println("7. Výpis všech studentů podle příjmení");
            System.out.println("8. Výpis obecného studijního průměru v obou oborech");
            System.out.println("9. Výpis počtu studentů v jednotlivých skupinách");
            System.out.println("10. Uložení vybraného studenta do souboru");
            System.out.println("11. Načtení vybraného studenta ze souboru");
            System.out.print("Vyber možnost: ");
            int volba = sc.nextInt();
            sc.nextLine();
            
            switch (volba) {
                case 1:
                    System.out.print("Zadej jméno: ");
                    String jmeno = sc.nextLine();
                    System.out.print("Zadej příjmení: ");
                    String prijmeni = sc.nextLine();
                    
                    int rok = 0;
                    boolean validniRok = false;
                    while (!validniRok) {
                        System.out.print("Zadej rok narození: ");
                        try {
                            rok = sc.nextInt();
                            validniRok = true;
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Neplatný formát roku narození. Zadejte prosím celé číslo.");
                            sc.nextLine(); 
                        }
                    }
                    sc.nextLine();
                    
                    System.out.print("Vyber obor (1 - Telekomunikace, 2 - Kyberbezpečnost): ");
                    int obor = sc.nextInt();
                    sc.nextLine();
                    
                    int id = databaze.getDalsiId();
                    Student novyStudent;
                    
                    if (obor == 1) {
                        novyStudent = new StudentTele(id, jmeno, prijmeni, rok);
                    } else {
                        novyStudent = new StudentKyb(id, jmeno, prijmeni, rok);
                    }
                    
                    databaze.pridejStudenta(novyStudent);
                    System.out.println("Student přidán s ID: " + id);
                    break;
                    
                case 2:
                    System.out.print("Zadej ID studenta: ");
                    int idZnamka = sc.nextInt();
                    sc.nextLine();
                    Student studentZnamka = databaze.najdiStudenta(idZnamka);
                    if (studentZnamka != null) {
                        System.out.print("Zadej známku (1-5): ");
                        int znamka = sc.nextInt();
                        sc.nextLine();
                        studentZnamka.pridejZnamku(znamka);
                        System.out.println("Známka přidána.");
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;
                    
                case 3:
                    System.out.print("Zadej ID studenta: ");
                    int idInfo = sc.nextInt();
                    sc.nextLine();
                    Student studentInfo = databaze.najdiStudenta(idInfo);
                    if (studentInfo != null) {
                        System.out.println("ID: " + studentInfo.getId());
                        System.out.println("Jméno: " + studentInfo.getJmeno());
                        System.out.println("Příjmení: " + studentInfo.getPrijmeni());
                        System.out.println("Rok narození: " + studentInfo.getRokNarozeni());
                        System.out.printf("Studijní průměr: %.2f\n", studentInfo.getStudijniPrumer());
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;
                    
                case 4:
                    System.out.print("Zadej ID studenta: ");
                    int idDovednost = sc.nextInt();
                    sc.nextLine();
                    Student studentDovednost = databaze.najdiStudenta(idDovednost);
                    if (studentDovednost != null) {
                        studentDovednost.spustDovednost();
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;
                    
                case 5:
                    System.out.print("Zadej ID studenta k odstranění: ");
                    int idSmazat = sc.nextInt();
                    sc.nextLine();
                    databaze.odeberStudenta(idSmazat);
                    break;
                    
                case 6:
                	databaze.ulozVsechnyStudentyDoDB();
                    konec = true;
                    System.out.println("Ukončuji program...");
                    break;
                    
                case 7:
                    databaze.vypisStudentyPodlePrijmeni();
                    break;
                    
                case 8:
                    databaze.vypocitejStudijniPrumery();
                    break;
                    
                case 9:
                    databaze.vypisPocetStudentuVeSkupinach();
                    break;
                    
                case 10:
                    System.out.print("Zadejte ID studenta pro uložení: ");
                    int idUlozeni = sc.nextInt();
                    databaze.ulozStudentaDoSouboru(idUlozeni);
                    break;
                    
                case 11:
                    System.out.print("Zadej název souboru pro načtení (např. student_1.txt): ");
                    String soubor = sc.nextLine();
                    databaze.nactiStudentaZeSouboru(soubor);
                    break;

                    
                default:
                    System.out.println("Neplatná volba.");
            }
        }

        sc.close();
    }
}
