public enum ResourceType {
    ORE(3),  // Example rarity values
    GAS(7),
    CRYSTAL(15),
    ENERGY(20),
    GEM(25),
    STONE(30);

    private final int rarity;

    ResourceType(int rarity) {
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }
}