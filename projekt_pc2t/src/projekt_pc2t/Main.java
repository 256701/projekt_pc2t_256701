package projekt_pc2t;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDatabase databaze = new StudentDatabase();

        boolean konec = false;
        int volba = -1;
        while (!konec) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Přidat nového studenta");
            System.out.println("2. Přidat známku studentovi");
            System.out.println("3. Zobrazit informace o studentovi");
            System.out.println("4. Spustit dovednost studenta");
            System.out.println("5. Odstranit studenta");
            System.out.println("6. Výpis všech studentů podle příjmení");
            System.out.println("7. Výpis obecného studijního průměru v obou oborech");
            System.out.println("8. Výpis počtu studentů v jednotlivých skupinách");
            System.out.println("9. Uložení vybraného studenta do souboru");
            System.out.println("10. Načtení vybraného studenta ze souboru");
            System.out.println("11. Konec");
            System.out.print("Vyber možnost: ");
            
            try {
                volba = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Neplatný vstup. Zadej číslo 1–11.");
                sc.nextLine();
                continue;
            }
            
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
                    
                    int obor = 0;
                    while (true) {
                        System.out.print("Vyber obor (1 - Telekomunikace, 2 - Kyberbezpečnost): ");
                        try {
                            obor = sc.nextInt();
                            sc.nextLine();
                            if (obor == 1 || obor == 2) {
                                break; 
                            } else {
                                System.out.println("Zadej prosím platnou volbu!");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup!");
                            sc.nextLine(); 
                        }
                    }
                    
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
                    int idZnamka = -1;
                    while (true) {
                        System.out.print("Zadej ID studenta: ");
                        try {
                            idZnamka = sc.nextInt();
                            sc.nextLine(); 
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup. Zadej prosím celé číslo.");
                            sc.nextLine();
                        }
                    }

                    Student studentZnamka = databaze.najdiStudenta(idZnamka);
                    if (studentZnamka != null) {
                        int znamka = -1;
                        while (true) {
                            System.out.print("Zadej známku (1-5): ");
                            try {
                                znamka = sc.nextInt();
                                sc.nextLine();
                                if (znamka >= 1 && znamka <= 5) {
                                    break;
                                } else {
                                    System.out.println("Známka musí být v rozsahu 1 až 5.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Neplatný vstup. Zadej číslo od 1 do 5.");
                                sc.nextLine();
                            }
                        }

                        studentZnamka.pridejZnamku(znamka);
                        System.out.println("Známka přidána.");
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;

                    
                case 3:
                    int idInfo = -1;
                    while (true) {
                        System.out.print("Zadej ID studenta: ");
                        try {
                            idInfo = sc.nextInt();
                            sc.nextLine(); 
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup. Zadej prosím celé číslo.");
                            sc.nextLine(); 
                        }
                    }

                    Student studentInfo = databaze.najdiStudenta(idInfo);
                    if (studentInfo != null) {
                        System.out.println("ID: " + studentInfo.getId());
                        System.out.println("Jméno: " + studentInfo.getJmeno());
                        System.out.println("Příjmení: " + studentInfo.getPrijmeni());
                        System.out.println("Rok narození: " + studentInfo.getRokNarozeni());
                        System.out.printf("Studijní průměr: %.2f\n", studentInfo.getStudijniPrumer());
                        if (studentInfo instanceof StudentTele) {
                            System.out.println("Obor: Telekomunikace");
                        } else if (studentInfo instanceof StudentKyb) {
                            System.out.println("Obor: Kyberbezpečnost");
                        } else {
                            System.out.println("Obor: Neznámý");
                        }
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;
               
                    
                case 4:
                    int idDovednost = -1;
                    while (true) {
                        System.out.print("Zadej ID studenta: ");
                        try {
                            idDovednost = sc.nextInt();
                            sc.nextLine(); 
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup. Zadej prosím celé číslo.");
                            sc.nextLine(); 
                        }
                    }

                    Student studentDovednost = databaze.najdiStudenta(idDovednost);
                    if (studentDovednost != null) {
                        studentDovednost.spustDovednost();
                    } else {
                        System.out.println("Student nenalezen.");
                    }
                    break;
                   
                  
                case 5:
                    int idSmazat = -1;
                    while (true) {
                        System.out.print("Zadej ID studenta k odstranění: ");
                        try {
                            idSmazat = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup. Zadej prosím celé číslo.");
                            sc.nextLine();
                        }
                    }
                    databaze.odeberStudenta(idSmazat);
                    break;
                    
                    
                case 11:
                	databaze.ulozVsechnyStudentyDoDB();
                    konec = true;
                    System.out.println("Ukončuji program...");
                    break;
                    
                    
                case 6:
                    databaze.vypisStudentyPodlePrijmeni();
                    break;
                    
                    
                case 7:
                    databaze.vypocitejStudijniPrumery();
                    break;
                    
                    
                case 8:
                    databaze.vypisPocetStudentuVeSkupinach();
                    break;
                    
                    
                case 9:
                    int idUlozeniSoubor = -1;
                    while (true) {
                        System.out.print("Zadejte ID studenta pro uložení do souboru: ");
                        try {
                            idUlozeniSoubor = sc.nextInt();
                            sc.nextLine();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup. Zadejte prosím celé číslo pro ID.");
                            sc.nextLine();
                        }
                    }
                    databaze.ulozStudentaDoSouboru(idUlozeniSoubor);
                    break;
                    
                    
                case 10:
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
