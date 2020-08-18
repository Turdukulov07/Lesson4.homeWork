import java.util.Random;

public class Main {


    public static int bossHealth = 1500;
    public static int bossDamage = 50;
    public static String bossDefenceType = "";
    public static int[] heroesHealth = {250, 260, 270, 500, 600, 230, 200, 200};
    public static int[] heroesDamage = {20, 15, 25, 0, 5, 10, 15,25};
    public static String[] heroesAttackType = {"Physical",
            "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber = 0;
    public static boolean isSleeping = false;

    public static void main(String[] args) {
        System.out.println("Game starts");
        printStatistics();
        while (!isGameFinished()) {
            roundNumber++;
            System.out.println(" +++++++++++++++++++  Round:   " + roundNumber + " +++++++++++++");
            round();
        }
    }

    public static void changeBossDefence() {
        Random r = new Random();
        int randomIndex = r.nextInt(heroesAttackType.length); //0,1,2
        bossDefenceType = heroesAttackType[randomIndex];
        for (int i = 0; i < bossDefenceType.length(); i++) {
            if (bossDefenceType == heroesAttackType[i] && heroesHealth[i] == 0) {
                continue;
            } else {
                System.out.println("Boss choose " + bossDefenceType);
                break;
            }


        }


//if random falls  to medic the Boss will get medic treatment too , cause they  gave  Hippocratic Oath (will help everybody)
        if (bossDefenceType == heroesAttackType[3]) {
            Random med = new Random();
            int helpBoss = med.nextInt(10) + 1;
            bossHealth = bossHealth + helpBoss;
            System.out.println("Boss get help from medic for " + helpBoss + " lives");
        }
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] <= bossDamage) {
                heroesHealth[i] = 0;
            }
        }
    }

    public static void round() {
        changeBossDefence();
        heroesHit();
        if (bossHealth > 0) {
            bossHits();
        }
        System.out.println(" Monitoring of Medic for each round ********* ");
        if (heroesHealth[3] == 0) {
            System.out.println("The doctor has been killed ////////// ");
        }
        medicalTreat();
        printStatistics();
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void bossHits() {
        if (!isSleeping) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0) {
                    if (heroesHealth[i] < bossDamage || heroesHealth[i] < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] = heroesHealth[i] - bossDamage;
                    }
                }
            }
        } else {
            isSleeping = false;
        }
        playerGolem();
        playerLucky();
        playerBerserk();
        playerThor();
    }



    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0) {
                if (bossHealth > 0) {
                    if (bossDefenceType == heroesAttackType[i]) {
                        Random r = new Random();
                        int coef = r.nextInt(8) + 2; //2,3,4,5,6,7,8,9
                        if (bossHealth - heroesDamage[i] * coef < 0) {
                            bossHealth = 0;
                        } else {
                            bossHealth = bossHealth - heroesDamage[i] * coef;
                        }
                        System.out.println("Critical attack: "
                                + (heroesDamage[i] * coef));
                    } else {
                        if (bossHealth - heroesDamage[i] < 0) {
                            bossHealth = 0;
                        } else {
                            bossHealth = bossHealth - heroesDamage[i];
                        }
                    }
                } else {
                    break;
                }
            }

        }
    }

    public static void printStatistics() {
        System.out.println("__________________");
        System.out.println("Boss health: " + bossHealth);
        for (int i = 0; i < heroesAttackType.length; i++) {
            System.out.println(heroesAttackType[i]
                    + " Health: " + heroesHealth[i]);
        }
    }

    public static void medicalTreat() {
        Random m = new Random();
        int heal = m.nextInt(70);
        for (int i = 0; i < heroesAttackType.length; i++) {
            if (heroesHealth[i] < 100 && heroesHealth[3] > 0 && heroesHealth[i] > 0) {
                heroesHealth[i] += heal;
                System.out.println("medic helped " + heroesAttackType[i] + " to " + heal + " point");
                return;
            }
        }
    }

    //    new method for Golen has increased life but weak hit. Can take 1/5 of the boss's hit to other players.
    public static void playerGolem() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[4] > 0 && heroesHealth[i] > 0 && !isSleeping) {
                heroesHealth[4] = heroesHealth[4] - (bossDamage / 5);
                heroesHealth[i] = heroesHealth[i] + (bossDamage / 5);
            }
        }
    }/* End of playrGolem method */

    public static void playerLucky() {
        Random r = new Random();
        int luckRandom = r.nextInt(2) + 1;
        if (heroesHealth[5] > 0) {
            if (luckRandom == 1) {
                heroesHealth[5] += bossDamage;
                System.out.println("Our lucky hero got luck ");
            }

        }

    }

    //    gets hit , blocks random amount of hit and returns back with his damage power;
    public static void playerBerserk() {
        Random p = new Random();
        int blockRandom = p.nextInt(30) + 1;
        if (!isSleeping) {
            if (heroesHealth[6] > 0) {
                heroesHealth[6] += blockRandom;
                bossHealth -= (heroesDamage[6] + blockRandom);
                System.out.println("This time blocked  " + blockRandom);
            }
        }
    }


    //    Thor method which can ability to make sleep the boss, and will not hit on that time ;
    public static void playerThor() {
        Random m = new Random();
        int ranThor = m.nextInt(2) + 1;
        System.out.println(ranThor);
        if (ranThor == 1) {
            isSleeping = true;
            System.out.println("boss sleeping  zZzZzZzZzZzZzZzZz");
        }
    } /*end of thor method*/
}/* end of the main */