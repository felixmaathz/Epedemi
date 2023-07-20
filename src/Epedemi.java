public class Epedemi {
    public static void main(String[] args) {

        boolean isVaccinated;
        isVaccinated = true;
        int vaccinatedVillage = deathCount(isVaccinated);
        isVaccinated = false;
        int notVaccinatedVillage = deathCount(isVaccinated);
        System.out.println("Antalet döda i vaccinerad by: " + vaccinatedVillage);
        System.out.println("Antalet döda i ovaccinerad by: " + notVaccinatedVillage);
    }


    static int deathCount(boolean isVaccinated) {
        int numberOfVillages = 5;
        int sum = 0;
        int middle;


        for(int i = 0; i < numberOfVillages; i++) {
            Village people = new Village(isVaccinated);
            double peopleSick = people.countSick();
            double peopleDead = people.countDead();

            while (peopleSick > 0) {
                people.dayPassesAll();
                peopleSick = people.countSick();
                peopleDead = people.countDead();
            }
            sum += peopleDead;
        }
        middle = sum/numberOfVillages;

        return middle;
    }
}

class Village  {
    final int SIZE = 1000;
    Person [] population = new Person[SIZE];

    Village (boolean isVaccinated) {
        for(int i = 0; i <= SIZE-1; i++) {
            population[i] = new Person(isVaccinated);
        }
    }
    int countSick() {
        int numSick = 0;
        for (int i = 0; i<SIZE; i++) {
            if (population[i].isSick) {
                numSick ++;
            }
        }
        return numSick;
    }

    int countDead() {
        int numDead = 0;
        for (int i = 0; i<SIZE; i++) {
            if (population[i].isDead) {
                numDead ++;
            }
        }
        return numDead;
    }

    void dayPassesAll() {
        for (int i = 0; i<SIZE; i++) {
            population[i].dayPasses(population);
        }
    }
}

class Person {
    boolean isSick;
    boolean isDead;

    double xPos, yPos;
    final double RANGE = 10;

    final int DAYS_IMMUNE = 7;
    int daysLeftImmune = 0;

    final double VAC_EFF = 0.95;
    final double VAC_PROB = 0.95;

    final double INIT_SICK_PROB = 0.8;
    final double GET_WELL_PROB = 0.8;
    final double DIE_PROB = 0.1;
    double INFECT_PROB = 0.1;


    Person(boolean isVaccinated) {
        xPos = Math.random()*1000;
        yPos = Math.random()*1000;
        double randomValue2 = Math.random();
        if (isVaccinated == true && randomValue2 <= VAC_PROB) {
            INFECT_PROB = INFECT_PROB*(1-VAC_EFF);
        }
        double randomValue = Math.random();
        if (randomValue <= INIT_SICK_PROB) {
            isSick = true;
        }
        else {
            isSick = false;
        }
    }
    void dayPasses (Person[] allPersons) {
        for (Person victim : allPersons){
            if (this != victim) {
                infect(victim);
            }
        }

        double randomValue = Math.random();
        if (isSick == true && randomValue <= GET_WELL_PROB) {
            isSick = false;
            daysLeftImmune = DAYS_IMMUNE;
        }
        double randomValue2 = Math.random();
        if (isSick == true && randomValue2 <= DIE_PROB) {
            isSick = false;
            isDead = true;
        }
        if (daysLeftImmune != 0 && isSick == false) {
            daysLeftImmune --;
        }
    }

    void becomesInfected () {
        double randomValue = Math.random();
        if (isDead == false && randomValue <= INFECT_PROB && daysLeftImmune == 0) {
            isSick = true;
        }

    }

    void infect(Person victim) {
        double distance = Math.sqrt((this.xPos-victim.xPos)*(this.xPos-victim.xPos)+(this.yPos-victim.yPos)*(this.yPos-victim.yPos));
        if (distance <= RANGE) {
            victim.becomesInfected();
        }

    }

}