import java.util.Scanner;

public class sommarutmaningenOla {
    public static Scanner spelarInput = new Scanner(System.in);
    public static void main(String[] args) {

        int antDragna = 0;
        int antDragnaHuset = 0;
        int antDragnaSpelare = 0;
        int omgang = 0;


        //Först skriver vi ut en snygg start.
        System.out.println("***********************************");
        System.out.println("********* Olas Black Jack *********");
        System.out.println("***********************************");
        System.out.println();

        //Vi börjar med att skapa kortlekarna
        //Om vi vill kan vi be spelaren att ange hur många kortlekar som ska användas men vi gör inte det.

        int strlKortlek = 416;
        String[][] kortlekar = skapaKortlekar(strlKortlek);

        //Sedan skapar vi två arrayer en för huset och en för spelaren.
        //Maximalt 12 förekomster, lägsta värde på 11 förekomster = 21
        String[][] husetsKort = new String[11][2];
        String[][] spelarensKort = new String[11][2];

        //Först drar huset sina första två kort
        int antDrag = 2;
        husetsKort = dragKort(antDragna, antDrag, strlKortlek, husetsKort, kortlekar);
        antDragnaHuset = antDragnaHuset + antDrag;

        //Sedan drar spelaren sina första två kort
        spelarensKort = dragKort(antDragna, antDrag, strlKortlek, spelarensKort, kortlekar);
        antDragnaSpelare = antDragnaSpelare + antDrag;

        String vem = "Huset";
        presenteraDragnakort(kortlekar, husetsKort, vem, antDrag, omgang);
        System.out.println();
        vem = "Spelaren";
        presenteraDragnakort(kortlekar, spelarensKort, vem, antDrag, omgang);

        antDragna = antDrag;
        omgang = omgang + 1;
        int poangHuset = beraknaPoang(husetsKort, antDragna);
        int poangSpelare = beraknaPoang(spelarensKort, antDragna);
        String spelareDraIgen = "J";

        //För test - ändra värden nedan för att simulera vilka poäng som tilldelats.
        //poangHuset = 22;
        //poangSpelare = 18;
        //För test - ändra värden ovan för att simulera vilka poäng som tilldelats.

        //Har ingen fått 21 direkt börjar vi dra ytterligare kort.
        //Spelaren börjar tills den inte vill dra igen eller blivit tjock (fått över 21 poäng)
        while ((spelareDraIgen.equals("J"))
                &&
                (poangSpelare < 21)) {

            //Om spelaren inte stoppat eller fått 21 poäng ännu kan denne dra igen.
            System.out.println();
            System.out.println("Vill du dra igen? J/N");
            spelareDraIgen = spelarVal();
            System.out.println();

            //Om spelaren inte stoppat eller fått över 21 poäng drar vi igen
            if (spelareDraIgen.equals("J")) {
                antDrag = 1;
                antDragna = antDragnaSpelare;
                spelarensKort = dragKort(antDragna, antDrag, strlKortlek, spelarensKort, kortlekar);
                antDragna = antDragna + antDrag;
                antDragnaSpelare = antDragna;
                poangSpelare = beraknaPoang(spelarensKort, antDragna);
                System.out.println();
            }
            vem = "Spelaren";
            presenteraDragnakort(kortlekar, spelarensKort, vem, antDragnaSpelare, omgang);
        }
        //Spelaren blev tjock
        if (poangSpelare > 21) {
            //Vi skriver även ut husets kort. Inkl det dolda kortet.
            System.out.println();
            vem = "Huset";
            presenteraDragnakort(kortlekar, husetsKort, vem, antDragnaHuset, omgang);

            //Nu skriver vi ut att spelaren blev tjock
            vem = "Spelaren";
        }
        String andre = "Huset";                                                                               //Spelaren är nu klar eller tjock.
        kollaTjock(vem, andre, poangSpelare);

        //Om spelaren inte blev tjock skriver vi ut husets kort och kollar om huset blev tjockt.'
        //if ((poangHuset > 21) && (poangSpelare < 22)){

        if (poangSpelare < 22){
            System.out.println();
            vem = "Huset";
            presenteraDragnakort(kortlekar, husetsKort, vem, antDragnaHuset, omgang);
            andre = "Spelaren";
            kollaTjock(vem, andre, poangHuset);
        }

        //Är inga tjocka är det nu husets tur att dra tills det når över 16

        while ((poangHuset < 17)&&(poangSpelare<22)){
            antDrag = 1;
            antDragna = antDragnaHuset;
            husetsKort = dragKort(antDragna, antDrag, strlKortlek, husetsKort, kortlekar);
            antDragna = antDragna + antDrag;
            antDragnaHuset = antDragna;
            poangHuset = beraknaPoang(husetsKort, antDragna);

            System.out.println();

            vem = "Huset";
            presenteraDragnakort(kortlekar, husetsKort, vem, antDragnaHuset, omgang);

            andre = "Spelaren";
            kollaTjock(vem, andre, poangHuset);

        }
        //Om ingen blivit tjock ska vi kolla vinnaren nu och huset kommit över 16 kollar vi vinnaren.
        if ((poangHuset <22)&&(poangSpelare<22)) {

            //Nu kollar vi vem som vann
            kollaVinnare(poangHuset, poangSpelare);

        }
        //Skriv slutligen även ut huset och spelarens totalpoänger (för att se att det blev rätt:
        System.out.println();
        System.out.println("Husets poäng = "+poangHuset);
        System.out.println("Spelarens poäng = "+poangSpelare);
        System.exit(0);

    }

    public static void presenteraDragnakort (String[][] kortlekar, String[][] dragnaKort, String vem,
                                             int antDrag, int omgang ){
        int utskrivna = 0;

        System.out.println(vem+" har dragit följande kort: ");
        while (utskrivna < antDrag){
            int kortPosition = Integer.parseInt(dragnaKort[utskrivna][0]);
            String kortKod = kortlekar[kortPosition][0];
            //System.out.println("Kortets utseende: "+kortKod);
            if  (((vem.equals("Huset")) && (utskrivna == 1))&&(omgang == 0)) {
                System.out.print("Dolt kort");
                //Aktivera kod nedan för kontrollutskrift av dolt kort
                //System.out.print(" = ");
                //skrivUtKort(kortKod);
                //Aktivera kod ovan för kontrollutskrift av dolt kort
                System.out.println();

            }else {
                skrivUtKort(kortKod);

            }
            utskrivna = utskrivna + 1;
        }
    }

    public static String [][] skapaKortlekar (int strlKortlek){
        //8 kortlekar á 52 kort = 52 * 8 = 416 kort
        //Andra arrayraden är till för att hålla koll så att samma kort inte kan dras flera gånger
        String [][] kortlekar = new String [strlKortlek][2];

        int fargKortIndex = 1;
        //1=ess, 2=2 ... 10=10, 11=Knekt, 12=dam, 13=kung
        int fargIndex = 1;
        int fargTypIndex = 0;
        //A = Hjärter
        //B = Ruter
        //C = Spader
        //D = Klöver
        char [] fargTyp = new char []{
                'A','B','C','D'};

        //Kortleken typas alltså:
        //1A1 = Hjärter ess i första kortleken >< 8D13 = Klöver kung i åttende kortleken

        for (int kortIndex = 0; kortIndex < kortlekar.length; kortIndex++) {
            //String kortIndexS = (String.valueOf(fargIndex)+String.valueOf(fargTyp[fargTypIndex])
            //        +String.valueOf(fargKortIndex));
            String kortIndexS = (fargIndex)+(String.valueOf(fargTyp[fargTypIndex]))
                    +String.valueOf(fargKortIndex);
            kortlekar[kortIndex][0] = kortIndexS;
            // -- indikerar att kort ännu ej dragits.
            kortlekar[kortIndex][1] = "--";
            fargKortIndex = fargKortIndex + 1;
            if (fargKortIndex > 13){
                fargKortIndex = 1;
                fargTypIndex = fargTypIndex + 1;
            }
            //Om fyra färger hanterats. Nollställ och börja på ny kortlek (nya färger)
            if (fargTypIndex > 3){
                fargTypIndex = 0;
                fargIndex = fargIndex + 1;
            }

        }

        return kortlekar;

    }

    public static String[][] dragKort(int antSparade, int antDrag, int strlKortlek,
                                      String[][] dragnaKort, String[][] kortlekar){
        int antDragna = 0;


        while (antDragna < antDrag){

            int nastaKort = 1 + (int) (Math.random() * (strlKortlek-1));

            //Kontrollera om draget redan finns i arrayn kortlekar annars boka det
            //placera in det i arrayn

            int kontrollSumma = kontrolleraKort(nastaKort, kortlekar);

            //Blir kontrollerat kort bokat lägger vi det i array och räknar upp antal och poängsumma,
            //annars forsätter vi dra.
            if (kontrollSumma != 0) {
                dragnaKort[antSparade][0] = String.valueOf(nastaKort);
                antDragna = antDragna + 1;
                dragnaKort = poangSumma (antSparade, kontrollSumma, dragnaKort);
                antSparade = antSparade + 1;
            }
        }
        //System.out.println("Antal sparade: "+antSparade);
        return dragnaKort;
    }

    public static int kontrolleraKort(int kortNr, String[][] kortlekar) {

        int kontrollSumma = 0;

        if ((kortlekar[kortNr][1]).equals("--")) {
            String kortKod = kortlekar[kortNr][0];
            kontrollSumma = beraknaKontrollSumma(kortKod);
            // OLA
            //System.out.println("KortNr= " + kortNr);

            String kontrollSummaS = String.valueOf(kontrollSumma);
            kortlekar[kortNr][1] = kontrollSummaS;
        }

        return kontrollSumma;

    }

    public static String [][] poangSumma(int antSparade, int kontrollSumma, String[][] dragnaKort){

        String poangSummaS = "";
        //String kontrollSummaS = "";

        if (antSparade > 0) {
            //kontrollSummaS = String.valueOf(kontrollSumma);
            //Poangsumman beräknas genom att addera detta korts kontrollsumma med
            //poängsumman efter föregående kort.
            int poangSumma = Integer.parseInt(dragnaKort[antSparade - 1][1]) + kontrollSumma;
            poangSummaS = String.valueOf(poangSumma);

        }else if (antSparade == 0) {
            //Här hanterar vi första kortet där poängsumman = kontrollsumman.
            poangSummaS = String.valueOf(kontrollSumma);
        }
        //Spara senaste poangSumman tillsammans med senast dragna kort.
        dragnaKort[antSparade][1] = poangSummaS;

        return dragnaKort;
    }

    public static int beraknaKontrollSumma (String kortKod){


        String kortVarde = kortKod.substring(2);
        int kontrollSumma = Integer.parseInt(kortVarde);

        //Alla klädda kort = 10 poäng
        if (kontrollSumma > 10){
            kontrollSumma = 10;
        }

        //Ess = 11 poäng
        if (kontrollSumma == 1){
            kontrollSumma = 11;
        }

        return kontrollSumma;
    }

    public static void skrivUtKort(String kortKod){

        String kortVardeS = " ess";

        String farg = kortKod.substring(1,2);
        String kortNrS = kortKod.substring(2);

        //A = Hjärter
        //B = Ruter
        //C = Spader
        //D = Klöver
        switch (farg) {
            case "A":
                farg = "Hjärter";
                break;
            case "B":
                farg = "Ruter";
                break;
            case "C":
                farg = "Spader";
                break;
            default:
                farg = "Klöver";
                break;
        }
        switch (kortNrS) {
            case "2":
                kortVardeS = " två";
                break;
            case "3":
                kortVardeS = " tre";
                break;
            case "4":
                kortVardeS = " fyra";
                break;
            case "5":
                kortVardeS = " fem";
                break;
            case "6":
                kortVardeS = " sex";
                break;
            case "7":
                kortVardeS = " sju";
                break;
            case "8":
                kortVardeS = " åtta";
                break;
            case "9":
                kortVardeS = " nio";
                break;
            case "10":
                kortVardeS = " tio";
                break;
            case "11":
                kortVardeS = " knekt";
                break;
            case "12":
                kortVardeS = " dam";
                break;
            case "13":
                kortVardeS = " kung";
                break;
            default:
                break;
        }

        System.out.println(farg+kortVardeS);
    }

    public static int beraknaPoang(String[][] dragnaKort, int antalSparade){

        return Integer.parseInt(dragnaKort[antalSparade-1][1]);
    }

    public static void kollaTjock(String vem, String andre, int poang){

        if (poang > 21){
            System.out.println();
            System.out.println(vem+" blev tjock");
            System.out.println(andre+" vinner!");
        }
    }

    public static void kollaVinnare(int poangHuset, int poangSpelare){

        String vem = "";

        if (
                (((poangHuset<22)&&(poangSpelare<22))
                        &&((poangHuset>poangSpelare)||(poangHuset==poangSpelare)))
                        ||
                        ((poangHuset>21)&&(poangSpelare>21))
                        ||
                        ((poangHuset<22)&&(poangSpelare>21))
        )
        {
            vem = "Huset";
        }else{
            vem = "Spelaren";
        }

        System.out.println();
        System.out.println(vem+" vinner!");
    }

    public static String spelarVal(){
        boolean spelarValGjort = false;
        String spelarValIn = "N";


        while (!spelarValGjort) {

            spelarValIn = spelarInput.nextLine();

            switch (spelarValIn) {
                case "J":
                    spelarValGjort = true;
                    break;
                case "j":
                    spelarValIn = "J";
                    spelarValGjort = true;
                case "N":
                    spelarValGjort = true;
                    break;
                case "n":
                    spelarValIn = "N";
                    spelarValGjort = true;
                    break;
                default:
                    System.out.println("Felaktigt val, välj J eller N");
                    break;
            }
        }

        return spelarValIn;
    }

}
