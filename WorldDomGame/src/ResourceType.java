public enum ResourceType {
    ORE(2), // Common
    GAS(3), // Common
    CRYSTAL(2), // Uncommon
    ENERGY(3), // Uncommon
    GEM(1), // Rare
    STONE(6), // Common
    // Add more resource types as needed
    ;

    private final int rarity;

    ResourceType(int rarity) {
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }
}
