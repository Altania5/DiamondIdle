import java.util.ArrayList;
import java.util.Random;

public class SolarSystem {
    private String name;
    private ArrayList<Planet> planets;
    private Planet selectedPlanet;

    public SolarSystem(String name) {
        this.name = name;
        this.planets = new ArrayList<>(generatePlanets());
        this.selectedPlanet = planets.get(0);
    }

    private ArrayList<Planet> generatePlanets() {
        Random random = new Random();
        ArrayList<Planet> madePlanets = new ArrayList<>();
        int numberOfPlanets = 1 + random.nextInt(10);
    
        for (int i = 0; i < numberOfPlanets; i++) {
            int x = random.nextInt(800) + 100; // Example x range
            int y = random.nextInt(600) + 100; // Example y range
            int radius = random.nextInt(50) + 20; // Example radius range
            madePlanets.add(new Planet(x, y, radius, "Planet " + (i + 1)));
        }
    
        return madePlanets;
    }

    public void removePlanet(Planet planet) {
        planets.remove(planet);
    }

    public int size(){
        return this.planets.size();
    }

    public void clear(){
        this.planets.clear();
    }

    public void addPlanet(Planet p){
        planets.add(p);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public Planet getSelectedPlanet(){
        return this.selectedPlanet;
    }

    public Planet setCurrentPlanet(int x){
        return selectedPlanet = planets.get(x - 1);
        
    }
}
